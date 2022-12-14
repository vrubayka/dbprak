package parser;

import daos.*;
import entities.*;
import logging.ReadLog;
import logging.ReadingError;
import logging.exceptions.MissingProductNameException;
import org.hibernate.SessionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DresdenReader {
    private final Document doc;
    private final SessionFactory sessionFactory;

    public DresdenReader(Document doc, SessionFactory sessionFactory) {
        this.doc = doc;
        this.sessionFactory = sessionFactory;
    }

    /**
     * Reads root element (store) of XML and store address, calls set methods for address properties and
     * address and store inserts.
     */
    public void readStoreXml() {

        Element root = doc.getDocumentElement();
        NamedNodeMap storeAttributeMap = root.getAttributes();
        AddressEntity storeAddress = readStoreAddress(storeAttributeMap);
        long storeId = saveStore(storeAddress);

        readItems(root, storeId);
    }

    /**
     * Sets attributes in AddressEntity instance.
     *
     * @param attributeMap - A Map containing the address properties
     * @return the address with set properties
     * @see AddressEntity
     */

    private AddressEntity readStoreAddress(NamedNodeMap attributeMap) {
        AddressEntity storeAddress = new AddressEntity();
        storeAddress.setCity(attributeMap.getNamedItem("name").getNodeValue());
        storeAddress.setPostcode(attributeMap.getNamedItem("zip").getNodeValue());
        storeAddress.setStreetName(attributeMap.getNamedItem("street").getNodeValue());

        return storeAddress;
    }

    /**
     * Inserts address and store (with given address) into database.
     *
     * @param storeAddress - address of store
     * @return The ID of store inserted in database
     * @see AddressEntity
     * @see StoreEntity
     */

    private long saveStore(AddressEntity storeAddress) {
        GenericDao<AddressEntity> addressEntityDao = new GenericDao<>(AddressEntity.class,
                                                                      sessionFactory);
        addressEntityDao.create(storeAddress);

        long addressId = storeAddress.getAddressId();
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setAddressId(addressId);
        storeEntity.setStoreName(storeAddress.getCity());
        GenericDao<StoreEntity> storeEntityDao = new GenericDao<>(StoreEntity.class, sessionFactory);
        storeEntityDao.create(storeEntity);

        return storeEntity.getStoreId();
    }

    /**
     * Iterates over child nodes of root element and calls read method for every found item element.
     *
     * @param root - root element of xml document
     * @param storeId - the ID of corresponding StoreEntity in database
     */

    private void readItems(Element root, long storeId) {
        for (Node currentNode = root.getFirstChild(); currentNode != null; currentNode = currentNode.getNextSibling()) {
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                if (currentNode.getNodeName().equals("item"))
                    try {
                        readItem(currentNode, storeId);
                    } catch (MissingProductNameException e) {
                        System.err.println(e.getMessage());
                    }
                else
                    System.err.println("Other elements than \"item\" in root scope.");
            }
        }
    }

    /**
     * Reads item data by iterating over child elements and calls of helper methods. Calls insert methods for
     * relevant entities of verified products.
     *
     * @param itemNode - node of item to read
     * @param storeId - ID of corresponding store
     *
     * @throws MissingProductNameException - if product Name is missing
     */
    private void readItem(Node itemNode, long storeId) throws MissingProductNameException {
        ProductEntity product = new ProductEntity();

        String group = readProdAndReturnGroup(itemNode, product);

        // read price, name and product info by pgroup
        //System.out.println(product.getProdId());
        if (product.getProdId() != null && group != null) {

            InventoryEntity inventoryEntry = new InventoryEntity();
            // read item data
            for (Node node = itemNode.getFirstChild(); node != null; node = node.getNextSibling()) {

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String scope = node.getNodeName();
                    if ("details".equals(scope)) {
                        readDetails(node, product);

                    }
                    if ("price".equals(scope)) {
                        readPrice(node, product, inventoryEntry, storeId);

                    } else if ("title".equals(scope)) {
                        product.setProdName(node.getFirstChild().getNodeValue().trim());

                    } else if ("bookspec".equals(scope) && "Book".equals(group)) {
                        checkProductName(product);

                        BookEntity book = new BookEntity();
                        List<PersonEntity> authorList = new ArrayList<>();

                        readBook(node, product, book, authorList);

                        insertBook(sessionFactory, product, book, authorList);
                        insertInventory(sessionFactory, inventoryEntry);

                    } else if ("dvdspec".equals(scope) && "DVD".equals(group)) {
                        checkProductName(product);

                        DvdEntity dvd = new DvdEntity();
                        List<PersonEntity> actorList = new ArrayList<>();
                        List<PersonEntity> creatorList = new ArrayList<>();
                        List<PersonEntity> directorList = new ArrayList<>();

                        readDvd(node, product, dvd, actorList, creatorList, directorList);

                        insertDvd(sessionFactory, product, dvd, actorList, creatorList, directorList);
                        insertInventory(sessionFactory, inventoryEntry);

                    } else if ("musicspec".equals(scope) && "Music".equals(group)) {
                        checkProductName(product);

                        CdEntity cd = new CdEntity();
                        List<TitleEntity> titleList = new ArrayList<>();
                        List<ArtistEntity> artistList = new ArrayList<>();

                        readCd(node, product, cd, titleList, artistList);

                        insertCd(sessionFactory, product, cd, titleList, artistList);
                        insertInventory(sessionFactory, inventoryEntry);
                    }
                }
            }


        }
    }

    /**
     * Reads and sets product ID, product group (Book, CD or DVD) and link to product image.
     *
     * @param itemNode - item node containing product attributes
     * @param product - ProductEntity instance
     *
     * @return product group as String
     *
     * @see ProductEntity
     */

    private String readProdAndReturnGroup(Node itemNode, ProductEntity product) {
        NamedNodeMap itemAttributes = itemNode.getAttributes();

        // check asin existence and set as prodId (primary key)
        if (itemAttributes.getNamedItem("asin") == null ||
            itemAttributes.getNamedItem("asin").getNodeValue().equals("")) {

            ReadLog.addError(new ReadingError("Product", null, "asin", "Missing or empty asin attribute."));

        } else {
            product.setProdId(itemAttributes.getNamedItem("asin").getNodeValue());
        }

        // check pgroup existence
        String pgroup = null;
        if (itemAttributes.getNamedItem("pgroup") == null ||
            itemAttributes.getNamedItem("pgroup").getNodeValue().equals("")) {

            ReadLog.addError(new ReadingError("Product", product.getProdId(), "pgroup",
                                              "Missing or empty pgroup attribute."));

        } else {
            pgroup = itemAttributes.getNamedItem("pgroup").getNodeValue();
        }

        return pgroup;

    }

    /**
     * Reads price and state of product. Sets these properties in InventoryEntity. Euro is the only accepted currency.
     *
     * @param priceNode - price node of item in XML file
     * @param product - corresponding ProductEntity instance
     * @param inventoryEntry - corresponding InventoryEntity instance
     * @param storeId - corresponding StoreEntity ID as long
     *
     * @see InventoryEntity
     */

    private void readPrice(Node priceNode, ProductEntity product, InventoryEntity inventoryEntry, long storeId) {
        NamedNodeMap priceAttributes = priceNode.getAttributes();
        inventoryEntry.setProdId(product.getProdId());
        inventoryEntry.setStoreId(storeId);
        inventoryEntry.setCondition(priceAttributes.getNamedItem("state").getNodeValue());


        // ToDo: exceptions other currencies
        if (priceAttributes.getNamedItem("currency").getNodeValue().equals("EUR")) {
            double mult = Double.parseDouble(priceAttributes.getNamedItem("mult").getNodeValue());
            double price = Double.parseDouble(priceNode.getFirstChild().getNodeValue()) * mult;
            if (price > 0)
                inventoryEntry.setPrice(new BigDecimal(price));
            else
                ReadLog.addError(new ReadingError("Inventory", product.getProdId(), "price",
                                                  "Price is zero or negative, price set to null."));

        } else if (priceAttributes.getNamedItem("currency").getNodeValue().length() > 0) {
            ReadLog.addError(new ReadingError("Product", product.getProdId(), "price",
                                              "Unknown currency, price not set."));
        }
    }

    /**
     * Reads relevant data for books. First iterates over bookspec node, then gets remaining book properties over
     * sibling nodes of bookspec.
     *
     * @param node - bookspec node of item in XML file
     * @param product - corresponding ProductEntity instance
     * @param book - corresponding BookEntity instance
     * @param authorList - empty list to be filled with authors (PersonEntity)
     *
     * @see BookEntity
     * @see PersonEntity
     */

    private void readBook(Node node, ProductEntity product, BookEntity book, List<PersonEntity> authorList) {
        book.setBookId(product.getProdId());
        for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                String scope = childNode.getNodeName();
                switch (scope) {
                    case "isbn":
                        // ToDo: ISBN sometimes missing or empty string, problem? currently inserted with "0"
                        String isbn = childNode.getAttributes().getNamedItem("val").getNodeValue();
                        if (isbn == null || isbn.equals("")) {
                            book.setIsbn("0");
                            ReadLog.addError(new ReadingError("Book", product.getProdId(), "isbn",
                                                              "No ISBN attribute or empty, ISBN set to 0."));
                        } else {
                            book.setIsbn(isbn);
                        }
                        break;
                    case "pages":
                        Node pageValue = childNode.getFirstChild();
                        if (pageValue == null || pageValue.getNodeValue().equals("")) {
                            ReadLog.addError(new ReadingError("Book", product.getProdId(), "pages",
                                                              "No pages attribute or empty, pages set to zero."));
                        } else if (Integer.parseInt(pageValue.getNodeValue()) <= 0) {
                            ReadLog.addError((new ReadingError("Book", product.getProdId(), "pages",
                                                               "Pages zero or negative, pages set to zero.")));
                        } else
                            book.setPages(Integer.parseInt(pageValue.getNodeValue()));
                        break;
                    case "publication":
                        String dateAsString = childNode.getAttributes().getNamedItem("date").getNodeValue();
                        if (dateAsString.equals("")) {
                            ReadLog.addError(new ReadingError("Book", product.getProdId(), "publication",
                                                              "Empty string in date attribute, date set to null."));
                        } else {
                            Date date = Date.valueOf(dateAsString);
                            Date currentDate = new Date(Calendar.getInstance().getTimeInMillis());
                            if (date.compareTo(currentDate) <= 0)
                                book.setReleaseDate(date);
                            else
                                ReadLog.addError(new ReadingError("Book", product.getProdId(), "publication",
                                                                  "Publication date in the future, date set to null."));
                        }
                        break;
                }
            }
        }

        for (Node sibling = node.getPreviousSibling(); sibling != null; sibling = sibling.getPreviousSibling()) {

            if (sibling.getNodeType() == Node.ELEMENT_NODE && sibling.getNodeName().equals("publishers")) {
                for (Node publishersNode = sibling.getFirstChild(); publishersNode != null; publishersNode =
                        publishersNode.getNextSibling()) {
                    if (publishersNode.getNodeType() == Node.ELEMENT_NODE &&
                        publishersNode.getNodeName().equals("publisher")) {
                        String publisher = publishersNode.getFirstChild().getNodeValue();
                        book.setPublisher(publisher);
                        // set Node to last Node to break for-loop
                        publishersNode = sibling.getLastChild();
                    }
                }
                // ToDo: no publisher ok?
                if(book.getPublisher() == null) {
                    book.setPublisher("");
                    ReadLog.addError(new ReadingError("Book", book.getBookId(), "publisher",
                                                      "Book has no publisher. Set to empty String."));
                }
            } else if (sibling.getNodeType() == Node.ELEMENT_NODE && sibling.getNodeName().equals("authors") &&
                       sibling.hasChildNodes()) {

                for (Node authorNode = sibling.getFirstChild(); authorNode != null;
                     authorNode = authorNode.getNextSibling()) {

                    if (authorNode.getNodeType() == Node.ELEMENT_NODE && authorNode.getNodeName().equals("author")) {
                        String authorName = authorNode.getFirstChild().getNodeValue();
                        PersonEntity author = new PersonEntity();
                        author.setPersonName(authorName);
                        authorList.add(author);
                    }
                }

                if (sibling.getNodeName().equals("authors")) {
                    // set Node to last Node to break for-loop
                    sibling = sibling.getFirstChild();
                }
            }
        }
    }

    /**
     * Reads relevant data for DVDs. First iterates over dvdspec node, then gets remaining DVD properties over
     * sibling nodes of dvdspec.
     *
     * @param node - dvdspec node of item in XML file
     * @param product - corresponding ProductEntity instance
     * @param dvd - corresponding DvdEntity instance
     * @param actorList - empty list to be filled with actors (PersonEntity)
     * @param creatorList - empty list to be filled with creators (PersonEntity)
     * @param directorList - empty list to be filled with directors (PersonEntity)
     *
     * @see DvdEntity
     * @see PersonEntity
     */

    private void readDvd(Node node, ProductEntity product, DvdEntity dvd, List<PersonEntity> actorList,
                         List<PersonEntity> creatorList, List<PersonEntity> directorList) {

        dvd.setDvdId(product.getProdId());

        for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                String scope = childNode.getNodeName();
                switch (scope) {
                    case "format":
                        dvd.setFormat(childNode.getFirstChild().getNodeValue());
                        break;
                    case "regioncode":
                        // ToDo: regionCode null ok?
                        if (childNode.hasChildNodes())
                            dvd.setRegionCode(Integer.parseInt(childNode.getFirstChild().getNodeValue()));
                        break;
                    case "runningtime":
                        // ToDo: change timeInSec to minutes
                        // ToDo: Term null ok?
                        if (childNode.hasChildNodes())
                            dvd.setTermInSec(Integer.parseInt(childNode.getFirstChild().getNodeValue()) * 60);
                        break;
                }
            }
        }



        for (Node sibling = node.getPreviousSibling(); sibling != null; sibling = sibling.getPreviousSibling()) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE &&
                (sibling.getNodeName().equals("actors") || sibling.getNodeName().equals("creators") ||
                 sibling.getNodeName().equals("directors")) &&
                sibling.hasChildNodes()) {

                for (Node roleNode = sibling.getFirstChild(); roleNode != null; roleNode =
                        roleNode.getNextSibling()) {

                    if (roleNode.getNodeType() == Node.ELEMENT_NODE && roleNode.getNodeName().equals("actor")) {
                        String actorName = roleNode.getFirstChild().getNodeValue();
                        PersonEntity actor = new PersonEntity();
                        actor.setPersonName(actorName);
                        actorList.add(actor);
                    } else if (roleNode.getNodeType() == Node.ELEMENT_NODE &&
                            roleNode.getNodeName().equals("creator")) {
                        String creatorName = roleNode.getFirstChild().getNodeValue();
                        PersonEntity creator = new PersonEntity();
                        creator.setPersonName(creatorName);
                        creatorList.add(creator);
                    } else if (roleNode.getNodeType() == Node.ELEMENT_NODE &&
                            roleNode.getNodeName().equals("director")) {
                        String directorName = roleNode.getFirstChild().getNodeValue();
                        PersonEntity director = new PersonEntity();
                        director.setPersonName(directorName);
                        directorList.add(director);
                    }
                }

                if (sibling.getNodeName().equals("actors")) {
                    // set Node to last Node to break for-loop
                    sibling = sibling.getFirstChild();
                }
            }
        }
    }

    /**
     * Reads relevant data for CDs. First iterates over musicspec node, then gets remaining CD properties over
     * sibling nodes of musicspec.
     *
     * @param node - musicspec node of item in XML file
     * @param product - corresponding ProductEntity instance
     * @param cd - corresponding CdEntity instance
     * @param titleList empty list to be filled with CD tracks (TitleEntity)
     * @param artistList empty list to be filled with CD artists (ArtistEntity)
     *
     * @see CdEntity
     * @see ArtistEntity
     */

    private void readCd(Node node, ProductEntity product, CdEntity cd, List<TitleEntity> titleList,
                        List<ArtistEntity> artistList) {

        cd.setCdId(product.getProdId());

        // read musicspec scope and set values (releasedate)
        for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                String scope = childNode.getNodeName();
                if ("releasedate".equals(scope)) {
                    if (childNode.hasChildNodes()) {
                        Date date = Date.valueOf(childNode.getFirstChild().getNodeValue());
                        Date currentDate = new Date(Calendar.getInstance().getTimeInMillis());
                        if (date.compareTo(currentDate) <= 0)
                            cd.setReleaseDate(date);
                        else
                            ReadLog.addError(new ReadingError("CD", product.getProdId(), "releasedate",
                                                              "Date is in the future, date set to null."));
                    } else {
                        ReadLog.addError(new ReadingError("CD", product.getProdId(), "releasedate",
                                                          "No value in releasedate, date set to null."));
                    }
                }
            }
        }

        // set CD Label, get Titles and Artists
        for (Node sibling = node.getPreviousSibling(); sibling != null; sibling = sibling.getPreviousSibling()) {

            if (sibling.getNodeType() == Node.ELEMENT_NODE && sibling.getNodeName().equals("labels")) {

                for (Node labelsNode = sibling.getFirstChild(); labelsNode != null; labelsNode =
                        labelsNode.getNextSibling()) {
                    if (labelsNode.getNodeType() == Node.ELEMENT_NODE && labelsNode.getNodeName().equals("label")) {
                        String label = labelsNode.getFirstChild().getNodeValue();
                        cd.setLabel(label);
                        // set Node to last Node to break for-loop
                        labelsNode = sibling.getLastChild();
                    }
                }
                // ToDo: label null value accepted?
                if (cd.getLabel() == null) {
                    cd.setLabel("none");
                    ReadLog.addError(new ReadingError("CD", product.getProdId(), "label",
                                                      "CD has no label, set to  \"none\""));
                }


            } else if (sibling.getNodeType() == Node.ELEMENT_NODE && sibling.getNodeName().equals("tracks")) {

                for (Node tracksNode = sibling.getFirstChild(); tracksNode != null; tracksNode =
                        tracksNode.getNextSibling()) {
                    if (tracksNode.getNodeType() == Node.ELEMENT_NODE && tracksNode.getNodeName().equals("title")) {
                        TitleEntity title = new TitleEntity();
                        title.setTitleName(tracksNode.getFirstChild().getNodeValue());
                        titleList.add(title);
                    }
                }
                // ToDo: creators = artist ok or error?
            } else if (sibling.getNodeType() == Node.ELEMENT_NODE &&
                       (sibling.getNodeName().equals("artists") || sibling.getNodeName().equals("creators")) &&
                       sibling.hasChildNodes()) {

                for (Node artistsNode = sibling.getFirstChild(); artistsNode != null; artistsNode =
                        artistsNode.getNextSibling()) {

                    if (artistsNode.getNodeType() == Node.ELEMENT_NODE &&
                            (artistsNode.getNodeName().equals("artist") || artistsNode.getNodeName().equals("creator"))) {

                        String artistName = artistsNode.getFirstChild().getNodeValue();
                        ArtistEntity artist = new ArtistEntity();
                        artist.setArtistName(artistName);

                        artistList.add(artist);
                    }
                }

                if (sibling.getNodeName().equals("artists")) {
                    // set Node to last Node to break for-loop
                    sibling = sibling.getFirstChild();
                }
            }
        }
    }

    public void readDetails(Node node, ProductEntity product) {
        String image = node.getAttributes().getNamedItem("img").getNodeValue();
        if (image.length() < 256)
            product.setImage(image);
        else {
            ReadLog.addError(new ReadingError("Product", product.getProdId(), "image",
                                              "Link is too long."));
        }
    }

    /**
     * Checks if product name is set. Throws exception to ignore this product in readItem method (product is not
     * further read).
     *
     * @param product - product to be checked for name
     *
     * @throws MissingProductNameException - if name is not set (=null)
     */
    public void checkProductName(ProductEntity product) throws MissingProductNameException {
        if (product.getProdName() == null) {
            ReadLog.addError(new ReadingError("Product", product.getProdId(), "prodName",
                    "Product has no name."));
            throw new MissingProductNameException("No product name in product: " + product.getProdId() + ".");
        }
    }

    /**
     * Inserts book (and product) and corresponding authors into database, if book (and product) is not already in
     * database. Creates corresponding AuthorEntity instances for given authors.
     *
     * @param sessionFactory - factory to create sessions in DAOs
     * @param product - product to be inserted
     * @param book - book to be inserted
     * @param authorList - list of persons, which are authors of this book
     */

    private void insertBook(SessionFactory sessionFactory, ProductEntity product, BookEntity book,
                            List<PersonEntity> authorList) {

        if (isNewProduct(product)) {
            ProductDao productDao = new ProductDao(sessionFactory);
            productDao.create(product);

            GenericDao<BookEntity> bookDao = new GenericDao<>(BookEntity.class, sessionFactory);
            bookDao.create(book);

            GenericDao<AuthorEntity> authorDao = new GenericDao<>(AuthorEntity.class, sessionFactory);
            AuthorEntity author = new AuthorEntity();
            for (PersonEntity person : authorList) {

                person = personPersistent(person);

                author.setPersonId(person.getPersonId());
                author.setBookId(book.getBookId());
                authorDao.create(author);
            }
        } else {
            ReadLog.addDuplicate(new ReadingError("Product", product.getProdId(), "Duplicate",
                                                  "Product already in Database."));
        }
    }

    /**
     * Inserts DVD (and product) and corresponding persons into database, if DVD (and product) is not already in
     * database. Creates instances of DVDPersonEntity by given lists with their roles in this DVD.
     *
     * @param sessionFactory - factory to create sessions in DAOs
     * @param product - product to be inserted
     * @param dvd - dvd to be inserted
     * @param actorList - list with persons, which are actors in this movie
     * @param creatorList - list with persons, which are creators of this movie
     * @param directorList - list with persons, which are directors of this movie
     *
     * @see DvdPersonEntity
     */

    private void insertDvd(SessionFactory sessionFactory, ProductEntity product, DvdEntity dvd,
                           List<PersonEntity> actorList, List<PersonEntity> creatorList,
                           List<PersonEntity> directorList) {

        if (isNewProduct(product)) {
            ProductDao productDao = new ProductDao(sessionFactory);
            productDao.create(product);

            GenericDao<DvdEntity> dvdDao = new GenericDao<>(DvdEntity.class, sessionFactory);
            dvdDao.create(dvd);

            GenericDao<DvdPersonEntity> dvdPersonDao = new GenericDao<>(DvdPersonEntity.class, sessionFactory);
            DvdPersonEntity dvdPerson = new DvdPersonEntity();

            for (PersonEntity actor : actorList) {

                actor = personPersistent(actor);

                dvdPerson.setPersonId(actor.getPersonId());
                dvdPerson.setDvdId(dvd.getDvdId());
                dvdPerson.setpRole("Actor");
                dvdPersonDao.create(dvdPerson);
            }

            for (PersonEntity creator : creatorList) {

                creator = personPersistent(creator);

                dvdPerson.setPersonId(creator.getPersonId());
                dvdPerson.setDvdId(dvd.getDvdId());
                dvdPerson.setpRole("Creator");
                dvdPersonDao.create(dvdPerson);
            }

            for (PersonEntity director : directorList) {

                director = personPersistent(director);

                dvdPerson.setPersonId(director.getPersonId());
                dvdPerson.setDvdId(dvd.getDvdId());
                dvdPerson.setpRole("Director");
                dvdPersonDao.create(dvdPerson);
            }
        } else {
            ReadLog.addDuplicate(new ReadingError("Product", product.getProdId(), "Duplicate",
                                                  "Product already in Database."));
        }
    }

    /**
     * Inserts CD (and product) and corresponding persons and tracks into database, if CD (and product) is not
     * already in database. Creates instances of CdArtistEntity and CdTitleEntity by given lists.
     *
     * @param sessionFactory - factory to create sessions in DAOs
     * @param product - product to be inserted
     * @param cd - CD to be inserted
     * @param titleList - list with tracks of this CD
     * @param artistList - list with artists of this CD
     *
     * @see CdArtistEntity
     * @see CdTitleEntity
     */

    private void insertCd(SessionFactory sessionFactory, ProductEntity product, CdEntity cd,
                          List<TitleEntity> titleList, List<ArtistEntity> artistList) {

        if (isNewProduct(product)) {
            ProductDao productDao = new ProductDao(sessionFactory);
            productDao.create(product);

            GenericDao<CdEntity> cdDao = new GenericDao<>(CdEntity.class, sessionFactory);
            cdDao.create(cd);

            GenericDao<CdArtistEntity> cdArtistDao = new GenericDao<>(CdArtistEntity.class, sessionFactory);
            CdArtistEntity cdArtist = new CdArtistEntity();

            for (ArtistEntity artist : artistList) {
                artist = artistPersistent(artist);

                cdArtist.setArtistId(artist.getArtistId());
                cdArtist.setCdId(cd.getCdId());

                if(isNewCdArtist(cdArtist)) {
                    cdArtistDao.create(cdArtist);
                }
            }

            GenericDao<CdTitleEntity> cdTitleDao = new GenericDao<>(CdTitleEntity.class, sessionFactory);
            CdTitleEntity cdTitle = new CdTitleEntity();

            for (TitleEntity title : titleList) {
                title = titlePersistent(title);

                cdTitle.setTitleId(title.getTitleId());
                cdTitle.setCdId(cd.getCdId());
                cdTitleDao.create(cdTitle);
            }

        } else {
            ReadLog.addDuplicate(new ReadingError("Product", product.getProdId(), "Duplicate",
                                                  "Product already in Database."));
        }
    }

    /**
     * Inserts inventory entry into database.
     *
     * @param sessionFactory - factory to create session in DAO
     * @param inventoryEntry - inventory entry to be inserted
     */

    private void insertInventory(SessionFactory sessionFactory, InventoryEntity inventoryEntry) {
        GenericDao<InventoryEntity> inventoryDao = new GenericDao<>(InventoryEntity.class, sessionFactory);
        inventoryDao.create(inventoryEntry);
    }

    /**
     * Check if given product is not in database.
     *
     * @param product - product to be checked
     *
     * @return true, if product is not in database, false otherwise
     */

    private boolean isNewProduct(ProductEntity product) {
        ProductDao productDao = new ProductDao(sessionFactory);
        if (productDao.findOne(product.getProdId()) == null) {
            return true;
        }
        return false;
    }

    /**
     * Checks if given CdArtist is not in database.
     *
     * @param cdArtist - CdArtist to be checked
     *
     * @return true, if CdArtist is not in database, false otherwise
     */

    private boolean isNewCdArtist(CdArtistEntity cdArtist) {
        CdArtistDao cdArtistDao = new CdArtistDao(sessionFactory);
        CdArtistEntityPK cdArtistPK = new CdArtistEntityPK();
        cdArtistPK.setCdId(cdArtist.getCdId());
        cdArtistPK.setArtistId(cdArtist.getArtistId());

        if(cdArtistDao.findOne(cdArtistPK) == null) {
            return true;
        }
        return false;
    }

    /**
     * Checks, if given person name is already in database. If not, inserts person. Returns PersonEntity in database.
     *
     * @param person - person, which name is checked
     *
     * @return PersonEntity read from database, if name already existed. PersonEntity inserted, if name was new.
     */

    private PersonEntity personPersistent(PersonEntity person) {

        PersonDao personDao = new PersonDao(sessionFactory);
        // check for existing person
        if (personDao.findByName(person.getPersonName()) == null) {
            personDao.create(person);
        } else {
            person = personDao.findByName(person.getPersonName());
        }

        return person;
    }

    /**
     * Checks, if given artist name is already in database. If not, inserts artist. Returns ArtistEntity in database.
     *
     * @param artist - artist, which name is checked
     *
     * @return ArtistEntity read from database, if name already existed. ArtistEntity inserted, if name was new.
     */

    private ArtistEntity artistPersistent(ArtistEntity artist) {

        ArtistDao artistDao = new ArtistDao(sessionFactory);
        // check for existing artist
        if (artistDao.findByName(artist.getArtistName()) == null) {
            artistDao.create(artist);
        } else {
            artist = artistDao.findByName(artist.getArtistName());
        }

        return artist;
    }

    /**
     * Checks, if given title name is already in database. If not, inserts title. Returns TitleEntity in database.
     *
     * @param title - title, which name is checked
     *
     * @return TitleEntity read from database, if name already existed. TitleEntity inserted, if name was new.
     */

    private TitleEntity titlePersistent(TitleEntity title) {

        TitleDao titleDao = new TitleDao(sessionFactory);
        // check for existing title
        if (titleDao.findByName(title.getTitleName()) == null) {
            titleDao.create(title);
        } else {
            title = titleDao.findByName(title.getTitleName());
        }

        return title;
    }
}
