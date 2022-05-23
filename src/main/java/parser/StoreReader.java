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
        NamedNodeMap itemAttributes = itemNode.getAttributes();
        ProductEntity product = new ProductEntity();
        // ToDo: check existens of these attributes
        product.setProdId(itemAttributes.getNamedItem("asin").getNodeValue());
        product.setImage(itemAttributes.getNamedItem("picture").getNodeValue());
        String group = itemAttributes.getNamedItem("pgroup").getNodeValue();

        InventoryEntity inventoryEntry = new InventoryEntity();
        BookEntity book = new BookEntity();


        for (Node node = itemNode.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String scope = node.getNodeName();
                if ("price".equals(scope)) {
                    readPrice(node, product, inventoryEntry, storeId);
                } else if ("title".equals(scope)) {
                    product.setProdName(node.getFirstChild().getNodeValue());
                } else if ("bookspec".equals(scope) && "Book".equals(group)) {
                    readBook(node, product, book);
                }
            }
        }

    }

    private void readPrice(Node priceNode, ProductEntity product, InventoryEntity inventoryEntry, long storeId) {
        NamedNodeMap priceAttributes = priceNode.getAttributes();
        inventoryEntry.setProdId(product.getProdId());
        inventoryEntry.setStoreId(storeId);
        inventoryEntry.setCondition(priceAttributes.getNamedItem("state").getNodeValue());
        //System.out.println(product.getProdId());

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
                switch(scope) {
                    case "isbn" :
                        book.setIsbn(childNode.getAttributes().getNamedItem("val").getNodeValue());
                        break;
                    case "pages" :
                        Node pageValue = childNode.getFirstChild();
                        if (pageValue == null){
                            book.setPages(0);
                        }
                        else
                        book.setPages(Integer.parseInt(pageValue.getNodeValue()));
                        break;
                    case "publication" :
                        book.setReleaseDate(Date.valueOf(childNode.getAttributes().getNamedItem("date").getNodeValue()));
                        break;
                }
            }
        }
    }
}
