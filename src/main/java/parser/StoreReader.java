package parser;

import daos.GenericDao;
import entities.*;
import org.hibernate.SessionFactory;
import org.w3c.dom.*;

import java.math.BigDecimal;
import java.sql.Date;

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
        InventoryEntity inventoryEntry = new InventoryEntity();
        BookEntity book = new BookEntity();
        DvdEntity dvd = new DvdEntity();
        CdEntity cd = new CdEntity();


        String group = readProdAndReturnGroup(itemNode, product);
        // ToDo: Exception asin/group == null
        System.out.println(product.getProdId());
        if (product.getProdId() != null && group != null) {
            for (Node node = itemNode.getFirstChild(); node != null; node = node.getNextSibling()) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String scope = node.getNodeName();
                    if ("price".equals(scope)) {
                        readPrice(node, product, inventoryEntry, storeId);
                    } else if ("title".equals(scope)) {
                        product.setProdName(node.getFirstChild().getNodeValue());
                    } else if ("bookspec".equals(scope) && "Book".equals(group)) {
                        readBook(node, product, book);
                    } else if ("dvdspec".equals(scope) && "DVD".equals(group)) {
                        readDvd(node, product, dvd);
                    } else if ("musicspec".equals(scope) && "Music".equals(group)) {
                        readCd(node, product, cd);
                    } /*else if ("labels".equals(scope) && "Music".equals(group)) {
                        readCdLabel(node, cd);
                    }*/
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
            inventoryEntry.setPrize(new BigDecimal(mult * price));
        }
    }

    private void readBook(Node node, ProductEntity product, BookEntity book) {
        book.setBookId(product.getProdId());
        for (Node childNode = node.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                String scope = childNode.getNodeName();
                switch (scope) {
                    case "isbn":
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
                        // ToDo: empty date String or verify with regular expression
                        if (!dateAsString.equals(""))
                            book.setReleaseDate(Date.valueOf(dateAsString));
                        break;
                }
            }
        }
        //node = <bookspec>
        for (Node loopNode = node; loopNode != null; loopNode = loopNode.getNextSibling()){
            if (loopNode.getNodeName().equals("publishers")){
                NodeList publisherNodeList = loopNode.getChildNodes();
                for (Node loopNodePub = loopNode.getFirstChild(); loopNodePub == loopNode.getLastChild();
                     loopNodePub = loopNodePub.getNextSibling() ){
                    if (loopNodePub.getNodeName().equals("publisher")){
                        System.out.println("Publisher is " + loopNodePub.getNodeValue());
                    }
                }
            }

        }
    }


    private void readDvd(Node node, ProductEntity product, DvdEntity dvd) {
        dvd.setDvdId(product.getProdId());
        // ToDo: what is MovieId for, delete?
        dvd.setMovieId(1);

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
    }

    private void readCd(Node node, ProductEntity product, CdEntity cd) {
        cd.setCdId(product.getProdId());

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
    }

    private void readCdLabel(Node node, CdEntity cd) {
        if (node.hasChildNodes())
            for (Node childNode = node.getFirstChild(); childNode.getNodeType() != Node.ELEMENT_NODE;
                 childNode = childNode.getNextSibling()) {
                cd.setLabel(childNode.getNodeValue());
            }
    }
}
