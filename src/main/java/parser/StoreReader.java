package parser;

import daos.GenericDao;
import entities.AddressEntity;
import entities.StoreEntity;
import org.hibernate.SessionFactory;
import org.w3c.dom.*;

public class StoreReader {

    private final Document doc;
    private final SessionFactory sessionFactory;

    public StoreReader(Document doc, SessionFactory sessionFactory) {
        this.doc = doc;
        this.sessionFactory = sessionFactory;
    }

    public void readStoreXml() {

        Element root = doc.getDocumentElement();
        NamedNodeMap storeAttributeMap = root.getAttributes();
        AddressEntity storeAddress = readStoreAddress(storeAttributeMap);
        saveStore(storeAddress);

        readItems(root);
    }

    private AddressEntity readStoreAddress(NamedNodeMap attributeMap) {
        AddressEntity storeAddress = new AddressEntity();
        storeAddress.setCity(attributeMap.getNamedItem("name").getNodeValue());
        storeAddress.setPostcode(attributeMap.getNamedItem("zip").getNodeValue());
        storeAddress.setStreetName(attributeMap.getNamedItem("street").getNodeValue());

        return storeAddress;
    }

    private void saveStore(AddressEntity storeAddress) {
        GenericDao<AddressEntity> addressEntityDao = new GenericDao<>(AddressEntity.class, sessionFactory);
        addressEntityDao.create(storeAddress);

        long addressId = storeAddress.getAddressId();
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setAddressId(addressId);
        storeEntity.setStoreName(storeAddress.getCity());
        GenericDao<StoreEntity> storeEntityDao= new GenericDao<>(StoreEntity.class, sessionFactory);
        storeEntityDao.create(storeEntity);
    }

    private void readItems(Element root) {

        for(Node currentNode = root.getFirstChild(); currentNode != null; currentNode = currentNode.getNextSibling()) {
            if(currentNode.getNodeType() == Node.ELEMENT_NODE) {
                if(currentNode.getNodeName().equals("item"))
                    readItem(currentNode);
                else
                    System.err.println("Other elements than \"item\" in root scope.");
            }
        }
    }

    private void readItem(Node currentNode) {
    }
}
