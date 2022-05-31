package parser;

import daos.ArtistDao;
import daos.GenericDao;
import daos.PersonDao;
import daos.TitleDao;
import entities.*;
import jakarta.persistence.PersistenceException;
import logging.ReadLog;
import logging.ReadingError;
import org.hibernate.SessionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.IOException;
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

    public void readStoreXml() {

        Element root = doc.getDocumentElement();
        NamedNodeMap storeAttributeMap = root.getAttributes();
        AddressEntity storeAddress = readStoreAddress(storeAttributeMap);
        long storeId = saveStore(storeAddress);

        readItems(root, storeId);
    }

    private AddressEntity readStoreAddress(NamedNodeMap attributeMap) {
        AddressEntity storeAddress = new AddressEntity();
        storeAddress.setCity(attributeMap.getNamedItem("name").getNodeValue());
        storeAddress.setPostcode(attributeMap.getNamedItem("zip").getNodeValue());
        storeAddress.setStreetName(attributeMap.getNamedItem("street").getNodeValue());

        return storeAddress;
    }

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

    private void readItems(Element root, long storeId) {
        for (Node currentNode = root.getFirstChild(); currentNode != null; currentNode = currentNode.getNextSibling()) {
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                if (currentNode.getNodeName().equals("item"))
                    readItem(currentNode, storeId);
                else
                    System.err.println("Other elements than \"item\" in root scope.");
            }
        }
    }

    private void readItem(Node itemNode, long storeId) {
        ProductEntity product = new ProductEntity();

        String group = readProdAndReturnGroup(itemNode, product);

        // read price, name and product info by pgroup
        System.out.println(product.getProdId());
        if (product.getProdId() != null && group != null) {

            InventoryEntity inventoryEntry = new InventoryEntity();
            // read item data
            for (Node node = itemNode.getFirstChild(); node != null; node = node.getNextSibling()) {

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String scope = node.getNodeName();

                    if ("price".equals(scope)) {
                        readPrice(node, product, inventoryEntry, storeId);

                    } else if ("details".equals(scope)) {
                        String image = node.getAttributes().getNamedItem("img").getNodeValue();
                        // check for link length
                        if (image.length() < 256) {
                            product.setImage(image);
                        } else
                            ReadLog.addError(new ReadingError("Product", product.getProdId(), "picture",
                                    "Link is too long."));
                    }
                    else if ("title".equals(scope)) {
                        // ToDo: check for empty title
                        product.setProdName(node.getFirstChild().getNodeValue());

                    } else if ("bookspec".equals(scope) && "Book".equals(group)) {
                        BookEntity book = new BookEntity();
                        List<PersonEntity> authorList = new ArrayList<>();

                        readBook(node, product, book, authorList);
                        // ToDo: check product/book values for not null and throw Exception
                        insertBook(sessionFactory, product, book, authorList);
                        insertInventory(sessionFactory, inventoryEntry);

                    } else if ("dvdspec".equals(scope) && "DVD".equals(group)) {
                        DvdEntity dvd = new DvdEntity();
                        List<PersonEntity> actorList = new ArrayList<>();
                        List<PersonEntity> creatorList = new ArrayList<>();
                        List<PersonEntity> directorList = new ArrayList<>();

                        readDvd(node, product, dvd, actorList, creatorList, directorList);
                        // ToDo: check product/dvd values for not null and throw Exception
                        if (product.getProdName() == null) {
                            product.setProdName("");
                        }
                        insertDvd(sessionFactory, product, dvd, actorList, creatorList, directorList);
                        insertInventory(sessionFactory, inventoryEntry);

                    } else if ("musicspec".equals(scope) && "Music".equals(group)) {
                        CdEntity cd = new CdEntity();
                        List<TitleEntity> titleList = new ArrayList<>();
                        List<ArtistEntity> artistList = new ArrayList<>();

                        readCd(node, product, cd, titleList, artistList);
                        // ToDo: check product/cd values for not null and throw Exception
                        if (product.getProdName() == null) {
                            product.setProdName("");
                        }
                        insertCd(sessionFactory, product, cd, titleList, artistList);
                        insertInventory(sessionFactory, inventoryEntry);
                    }
                }
            }


        }
    }


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

    private void readDetails(Node detailsNode){

    }


    private void readPrice(Node priceNode, ProductEntity product, InventoryEntity inventoryEntry, long storeId) {
        NamedNodeMap priceAttributes = priceNode.getAttributes();
        inventoryEntry.setProdId(product.getProdId());
        inventoryEntry.setStoreId(storeId);
        inventoryEntry.setCondition(priceAttributes.getNamedItem("state").getNodeValue());


        // ToDo: exceptions other currencies
        if (priceAttributes.getNamedItem("currency").getNodeValue().equals("EUR")) {
            double mult = Double.parseDouble(priceAttributes.getNamedItem("mult").getNodeValue());
            double price = Double.parseDouble(priceNode.getFirstChild().getNodeValue());
            inventoryEntry.setPrice(new BigDecimal(mult * price));

        } else if (priceAttributes.getNamedItem("currency").getNodeValue().length() > 0) {
            ReadLog.addError(new ReadingError("Product", product.getProdId(), "price",
                    "Unknown currency."));
        }

    }

    private void readBook(Node node, ProductEntity product, BookEntity book, List<PersonEntity> authorList) {
        book.setBookId(product.getProdId());
        for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                String scope = childNode.getNodeName();
                switch (scope) {
                    case "isbn":
                        // ToDo: ISBN sometimes empty string, problem? currently inserted with empty string
                        String isbn = childNode.getAttributes().getNamedItem("val").getNodeValue();
                        if(isbn != null) {
                            book.setIsbn(isbn);
                        } else {

                        }
                        break;
                    case "pages":
                        // ToDo: exception if no value
                        Node pageValue = childNode.getFirstChild();
                        if (pageValue == null)
                            book.setPages(0);
                        else
                            book.setPages(Integer.parseInt(pageValue.getNodeValue()));
                        break;
                    case "publication":
                        String dateAsString = childNode.getAttributes().getNamedItem("date").getNodeValue();
                        // ToDo: empty date String or verify with regular expression and remove else!!!
                        if (!dateAsString.equals(""))
                            book.setReleaseDate(Date.valueOf(dateAsString));
                        else
                            book.setReleaseDate(new Date(Calendar.getInstance().getTimeInMillis()));
                        break;
                }
            }
        }

        for (Node sibling = node.getPreviousSibling(); sibling != null; sibling = sibling.getPreviousSibling()) {

            if (sibling.getNodeType() == Node.ELEMENT_NODE && sibling.getNodeName().equals("publishers")) {
                //TODO: when publisher is missing
                for (Node publishersNode = sibling.getFirstChild(); publishersNode != null; publishersNode =
                        publishersNode.getNextSibling()) {
                    if (publishersNode.getNodeType() == Node.ELEMENT_NODE &&
                            publishersNode.getNodeName().equals("publisher")) {
                        String publisherName = publishersNode.getFirstChild().getNodeValue();
                        if (publisherName != null){
                            book.setPublisher(publisherName);
                        } else book.setPublisher("");
                        // set Node to last Node to break for-loop
                        publishersNode = sibling.getLastChild();
                    }
                }
                //TODO: delete when exception is done
                if (book.getPublisher() == null){
                    book.setPublisher("");
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

        // ToDo: DvdEntity has no studio, necessary?

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

    private void readCd(Node node, ProductEntity product, CdEntity cd, List<TitleEntity> titleList,
                        List<ArtistEntity> artistList) {

        cd.setCdId(product.getProdId());

        // read musicspec scope and set values (releasedate)
        for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                String scope = childNode.getNodeName();
                switch (scope) {
                    case "releasedate":
                        // ToDo: releaseDate null value ok? currently current date inserted
                        if (childNode.hasChildNodes())
                            cd.setReleaseDate(Date.valueOf(childNode.getFirstChild().getNodeValue()));
                        else
                            cd.setReleaseDate(new Date(Calendar.getInstance().getTimeInMillis()));
                        break;
                }
            }
        }

        // set CD Label, get Titles and Artists
        for (Node sibling = node.getPreviousSibling(); sibling != null; sibling = sibling.getPreviousSibling()) {

            if (sibling.getNodeType() == Node.ELEMENT_NODE && sibling.getNodeName().equals("labels")) {

                for (Node labelsNode = sibling.getFirstChild(); labelsNode != null; labelsNode =
                        labelsNode.getNextSibling()) {
                    if (labelsNode.getNodeType() == Node.ELEMENT_NODE && labelsNode.getNodeName().equals("label")) {
                        String labelName = labelsNode.getFirstChild().getNodeValue();
                        cd.setLabel(labelName);
                        // set Node to last Node to break for-loop
                        labelsNode = sibling.getLastChild();
                    }
                }
                // ToDo: label null value accepted?
                if (cd.getLabel() == null)
                    cd.setLabel("");


            } else if (sibling.getNodeType() == Node.ELEMENT_NODE && sibling.getNodeName().equals("tracks")) {

                for (Node tracksNode = sibling.getFirstChild(); tracksNode != null; tracksNode =
                        tracksNode.getNextSibling()) {
                    if (tracksNode.getNodeType() == Node.ELEMENT_NODE && tracksNode.getNodeName().equals("title")) {
                        TitleEntity title = new TitleEntity();
                        title.setTitleName(tracksNode.getFirstChild().getNodeValue());
                        titleList.add(title);
                    }
                }
                // ToDo: creators = artist or error?
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


    private void insertBook(SessionFactory sessionFactory, ProductEntity product, BookEntity book,
                            List<PersonEntity> authorList) {

        GenericDao<ProductEntity> productDao = new GenericDao<>(sessionFactory);
        productDao.create(product);

        GenericDao<BookEntity> bookDao = new GenericDao<>(sessionFactory);
        bookDao.create(book);

        GenericDao<AuthorEntity> authorDao = new GenericDao<>(sessionFactory);
        AuthorEntity author = new AuthorEntity();
        for (PersonEntity person : authorList) {

            person = personPersistent(person);

            author.setPersonId(person.getPersonId());
            author.setBookId(book.getBookId());
            authorDao.create(author);
        }
    }

    private void insertDvd(SessionFactory sessionFactory, ProductEntity product, DvdEntity dvd,
                           List<PersonEntity> actorList, List<PersonEntity> creatorList,
                           List<PersonEntity> directorList) {

        GenericDao<ProductEntity> productDao = new GenericDao<>(sessionFactory);
        productDao.create(product);

        GenericDao<DvdEntity> dvdDao = new GenericDao<>(sessionFactory);
        dvdDao.create(dvd);

        GenericDao<DvdPersonEntity> dvdPersonDao = new GenericDao<>(sessionFactory);
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
    }

    private void insertCd(SessionFactory sessionFactory, ProductEntity product, CdEntity cd,
                          List<TitleEntity> titleList, List<ArtistEntity> artistList) {

        GenericDao<ProductEntity> productDao = new GenericDao<>(sessionFactory);
        productDao.create(product);

        GenericDao<CdEntity> cdDao = new GenericDao<>(sessionFactory);
        cdDao.create(cd);

        GenericDao<CdArtistEntity> cdArtistDao = new GenericDao<>(sessionFactory);
        CdArtistEntity cdArtist = new CdArtistEntity();

        for (ArtistEntity artist : artistList) {
            artist = artistPersistent(artist);

            cdArtist.setArtistId(artist.getArtistId());
            cdArtist.setCdId(cd.getCdId());
            // ToDo: custom exception
            try {
                cdArtistDao.create(cdArtist);
            } catch (PersistenceException e) {
                System.err.println("Duplicate CDArtist found: " + cdArtist.getCdId() + " " + cdArtist.getArtistId());
            }
        }

        GenericDao<CdTitleEntity> cdTitleDao = new GenericDao<>(sessionFactory);
        CdTitleEntity cdTitle = new CdTitleEntity();

        for (TitleEntity title : titleList) {
            title = titlePersistent(title);

            cdTitle.setTitleId(title.getTitleId());
            cdTitle.setCdId(cd.getCdId());
            // ToDo: custom exception
            cdTitleDao.create(cdTitle);
        }
    }

    private void insertInventory(SessionFactory sessionFactory, InventoryEntity inventoryEntry) {
        GenericDao<InventoryEntity> inventoryDao = new GenericDao<>(sessionFactory);
        inventoryDao.create(inventoryEntry);
    }

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
