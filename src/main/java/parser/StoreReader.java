package parser;

import daos.GenericDao;
import daos.PersonDao;
import entities.*;
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


public class StoreReader {

    private final Document doc;
    private final SessionFactory sessionFactory;

    public StoreReader(Document doc, SessionFactory sessionFactory) {
        this.doc            = doc;
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
        GenericDao<AddressEntity> addressEntityDao = new GenericDao<>(AddressEntity.class, sessionFactory);
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
        // ToDo: Exception asin/group == null
        System.out.println(product.getProdId());
        if (product.getProdId() != null && group != null) {

            // read item data
            for (Node node = itemNode.getFirstChild(); node != null; node = node.getNextSibling()) {

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String scope = node.getNodeName();

                    if ("price".equals(scope)) {
                        InventoryEntity inventoryEntry = new InventoryEntity();
                        readPrice(node, product, inventoryEntry, storeId);

                    } else if ("title".equals(scope)) {
                        product.setProdName(node.getFirstChild().getNodeValue());

                    } else if ("bookspec".equals(scope) && "Book".equals(group)) {
                        BookEntity book = new BookEntity();
                        List<PersonEntity> authorList = new ArrayList<>();

                        readBook(node, product, book, authorList);
                        // ToDo: check product/book values for not null and throw Exception
                        insertBook(sessionFactory, product, book, authorList);

                    } else if ("dvdspec".equals(scope) && "DVD".equals(group)) {
                        DvdEntity dvd = new DvdEntity();
                        List<PersonEntity> actorList = new ArrayList<>();
                        List<PersonEntity> creatorList = new ArrayList<>();
                        List<PersonEntity> directorList = new ArrayList<>();

                        readDvd(node, product, dvd, actorList, creatorList, directorList);
                        // ToDo: check product/dvd values for not null and throw Exception
                        if(product.getProdName() == null) {
                            product.setProdName("");
                        }
                        insertDvd(sessionFactory, product, dvd, actorList, creatorList, directorList);

                    } else if ("musicspec".equals(scope) && "Music".equals(group)) {
                        CdEntity cd = new CdEntity();
                        List<TitleEntity> titleList = new ArrayList<>();
                        List<ArtistEntity> artistList = new ArrayList<>();

                        readCd(node, product, cd, titleList, artistList);
                        // ToDo: check product/cd values for not null and throw Exception
                        insertCd(sessionFactory, product, cd, titleList, artistList);
                    }
                }
            }


        }
    }

    private String readProdAndReturnGroup(Node itemNode, ProductEntity product) {
        NamedNodeMap itemAttributes = itemNode.getAttributes();
        // ToDo: check existens of these attributes
        if (itemAttributes.getNamedItem("asin") != null && itemAttributes.getNamedItem("pgroup") != null) {
            String asin = itemAttributes.getNamedItem("asin").getNodeValue();
            String picture = itemAttributes.getNamedItem("picture").getNodeValue();
            String pgroup = itemAttributes.getNamedItem("pgroup").getNodeValue();
            if (asin != "")
                product.setProdId(asin);
            product.setImage(picture);

            return pgroup;
        }
        return null;
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
        }
    }

    private void readBook(Node node, ProductEntity product, BookEntity book, List<PersonEntity> authorList) {
        book.setBookId(product.getProdId());
        for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                String scope = childNode.getNodeName();
                switch (scope) {
                    case "isbn":
                        // ToDo: ISBN sometimes empty string, problem?
                        book.setIsbn(childNode.getAttributes().getNamedItem("val").getNodeValue());
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

        for (Node sibling = node.getNextSibling(); sibling != null; sibling = sibling.getNextSibling()) {

            if (sibling.getNodeType() == Node.ELEMENT_NODE && sibling.getNodeName().equals("publishers")) {

                for (Node publishersNode = sibling.getFirstChild(); publishersNode != null; publishersNode =
                        publishersNode.getNextSibling()) {
                    if (publishersNode.getNodeType() == Node.ELEMENT_NODE &&
                        publishersNode.getNodeName().equals("publisher")) {
                        NamedNodeMap publisherAttributes = publishersNode.getAttributes();
                        book.setPublisher(publisherAttributes.getNamedItem("name").getNodeValue());
                        // set Node to last Node to break for-loop
                        publishersNode = sibling.getLastChild();
                    }
                }
            } else if (sibling.getNodeType() == Node.ELEMENT_NODE && sibling.getNodeName().equals("authors") &&
                  sibling.hasChildNodes()) {

                for (Node authorNode = sibling.getFirstChild(); authorNode != null;
                     authorNode = authorNode.getNextSibling()) {

                    if (authorNode.getNodeType() == Node.ELEMENT_NODE && authorNode.getNodeName().equals("author")) {
                        NamedNodeMap authorAttributes = authorNode.getAttributes();
                        PersonEntity author = new PersonEntity();
                        author.setPersonName(authorAttributes.getNamedItem("name").getNodeValue());
                        authorList.add(author);
                    }
                }

                if(sibling.getNodeName().equals("authors")) {
                    // set Node to last Node to break for-loop
                    sibling = sibling.getLastChild();
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

        for (Node sibling = node.getNextSibling(); sibling != null; sibling = sibling.getNextSibling()) {

            /*
            if (sibling.getNodeType() == Node.ELEMENT_NODE && sibling.getNodeName().equals("studios")) {

                for (Node studiosNode = sibling.getFirstChild(); studiosNode != null; studiosNode  =
                        studiosNode.getNextSibling()) {
                    if(studiosNode.getNodeType() == Node.ELEMENT_NODE && studiosNode.getNodeName().equals("studio")) {
                        NamedNodeMap studioNode = studiosNode.getAttributes();
                        dvd.set(studioNode.getNamedItem("name").getNodeValue());
                        // set Node to last Node to break for-loop
                        studiosNode = sibling.getLastChild();
                        sibling = sibling.getLastChild();
                    }
                }
            }

             */
            if (sibling.getNodeType() == Node.ELEMENT_NODE &&
                (sibling.getNodeName().equals("actors") || sibling.getNodeName().equals("creators") ||
                 sibling.getNodeName().equals("directors")) &&
                sibling.hasChildNodes()) {

                for (Node roleNode = sibling.getFirstChild(); roleNode != null; roleNode =
                        roleNode.getNextSibling()) {

                    if (roleNode.getNodeType() == Node.ELEMENT_NODE && roleNode.getNodeName().equals("actor")) {
                        NamedNodeMap actorAttributes = roleNode.getAttributes();
                        PersonEntity actor = new PersonEntity();
                        actor.setPersonName(actorAttributes.getNamedItem("name").getNodeValue());
                        actorList.add(actor);
                    } else if (roleNode.getNodeType() == Node.ELEMENT_NODE &&
                               roleNode.getNodeName().equals("creator")) {
                        NamedNodeMap creatorAttributes = roleNode.getAttributes();
                        PersonEntity creator = new PersonEntity();
                        creator.setPersonName(creatorAttributes.getNamedItem("name").getNodeValue());
                        creatorList.add(creator);
                    } else if (roleNode.getNodeType() == Node.ELEMENT_NODE &&
                               roleNode.getNodeName().equals("director")) {
                        NamedNodeMap directorAttributes = roleNode.getAttributes();
                        PersonEntity director = new PersonEntity();
                        director.setPersonName(directorAttributes.getNamedItem("name").getNodeValue());
                        directorList.add(director);
                    }
                }

                if(sibling.getNodeName().equals("directors")) {
                    // set Node to last Node to break for-loop
                    sibling = sibling.getLastChild();
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
                        // ToDo: releaseDate null value ok?
                        if (childNode.hasChildNodes())
                            cd.setReleaseDate(Date.valueOf(childNode.getFirstChild().getNodeValue()));
                        break;
                }
            }
        }

        // set CD Label, get Titles and Artists
        for (Node sibling = node.getNextSibling(); sibling != null; sibling = sibling.getNextSibling()) {

            if (sibling.getNodeType() == Node.ELEMENT_NODE && sibling.getNodeName().equals("labels")) {

                for (Node labelsNode = sibling.getFirstChild(); labelsNode != null; labelsNode =
                        labelsNode.getNextSibling()) {
                    if (labelsNode.getNodeType() == Node.ELEMENT_NODE && labelsNode.getNodeName().equals("label")) {
                        NamedNodeMap labelAttributes = labelsNode.getAttributes();
                        cd.setLabel(labelAttributes.getNamedItem("name").getNodeValue());
                        // set Node to last Node to break for-loop
                        labelsNode = sibling.getLastChild();
                    }
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
                // ToDo: creators = artist or error?
            } else if (sibling.getNodeType() == Node.ELEMENT_NODE &&
                       (sibling.getNodeName().equals("artists") || sibling.getNodeName().equals("creators")) &&
                       sibling.hasChildNodes()) {

                for (Node artistsNode = sibling.getFirstChild(); artistsNode != null; artistsNode =
                        artistsNode.getNextSibling()) {

                    if (artistsNode.getNodeType() == Node.ELEMENT_NODE &&
                        (artistsNode.getNodeName().equals("artist") || artistsNode.getNodeName().equals("creator"))) {

                        NamedNodeMap artistAttributes = artistsNode.getAttributes();
                        ArtistEntity artist = new ArtistEntity();
                        artist.setArtistName(artistAttributes.getNamedItem("name").getNodeValue());

                        artistList.add(artist);
                    }
                }
                if(sibling.getNodeName().equals("creators")) {
                    // set Node to last Node to break for-loop
                    sibling = sibling.getLastChild();
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
        for(PersonEntity person : authorList) {

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

        for(PersonEntity actor : actorList) {

            actor = personPersistent(actor);

            dvdPerson.setPersonId(actor.getPersonId());
            dvdPerson.setDvdId(dvd.getDvdId());
            dvdPerson.setpRole("Actor");
            dvdPersonDao.create(dvdPerson);
        }

        for(PersonEntity creator : creatorList) {

            creator = personPersistent(creator);

            dvdPerson.setPersonId(creator.getPersonId());
            dvdPerson.setDvdId(dvd.getDvdId());
            dvdPerson.setpRole("Creator");
            dvdPersonDao.create(dvdPerson);
        }

        for(PersonEntity director : directorList) {

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


    }
    private PersonEntity personPersistent(PersonEntity person) {

        PersonDao personDao = new PersonDao(sessionFactory);
        // check for existing person
        if(personDao.findByName(person.getPersonName()) == null) {
            personDao.create(person);
        } else {
            person = personDao.findByName(person.getPersonName());
        }

        return person;
    }
}
