package parser;

import daos.GenericDao;
import entities.AddressEntity;
import entities.StoreEntity;
import org.hibernate.SessionFactory;
import org.w3c.dom.*;

public class StoreReader {

    private Document doc;
    private SessionFactory sessionFactory;

    public StoreReader(Document doc, SessionFactory sessionFactory) {
        this.doc = doc;
        this.sessionFactory = sessionFactory;
    }

    public void readStoreXml() {

        Element root = doc.getDocumentElement();
        NamedNodeMap storeAttributeMap = root.getAttributes();
        AddressEntity storeAddress = readStoreAddress(storeAttributeMap);
        saveStore(storeAddress);

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

        Long addressId = storeAddress.getAddressId();
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setAddressId(addressId);
        storeEntity.setStoreName(storeAddress.getCity());
        GenericDao<StoreEntity> storeEntityDao= new GenericDao<>(StoreEntity.class, sessionFactory);
        storeEntityDao.create(storeEntity);
    }
}
