package parser;

import daos.GenericDao;
import entities.AddressEntity;
import entities.CategoryEntity;
import entities.ProductCategoryEntity;
import org.hibernate.SessionFactory;
import org.w3c.dom.*;
;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.util.HashMap;

public class XmlParser implements Reader{
    HashMap<String, Long> categoryDaoMap = new HashMap<>();
    @Override
    public void readFile(String filePath, SessionFactory sessionFactory) {
        File inputFile = new File(filePath);
        Document doc = getNormalizedDocument(inputFile);
        String rootElement = doc.getDocumentElement().getNodeName();
        switch (rootElement) {
            case "shop":
                System.out.println("Reading shop...");
                StoreReader storeReader = new StoreReader(doc, sessionFactory);
                storeReader.readStoreXml();

                System.out.println("Finished reading.");
        }
    }

    private Document getNormalizedDocument(File inputFile) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document doc = documentBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            return doc;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void parseCategories(NodeList list, SessionFactory sessionFactory) {

        for (int i = 0; i < list.getLength(); i++) {
            if (returnTagOfNode(list.item(i)).equals("category")) {

                GenericDao<CategoryEntity> daoCat = new GenericDao<>(sessionFactory);
                CategoryEntity entityCat = new CategoryEntity();

                entityCat.setCategoryName(returnTextValueOfNode(list.item(i)));
                daoCat.create(entityCat);
                categoryDaoMap.put(returnTextValueOfNode(list.item(i)), entityCat.getCategoryId());

                if (returnTagOfNode(list.item(i).getParentNode()).equals("category")){
                    entityCat.setSuperCategory(categoryDaoMap.get(returnTextValueOfNode(list.item(i))));
                }
                parseCategories(list.item(i).getChildNodes(), sessionFactory);

            }
            else if ((returnTagOfNode(list.item(i))).equals("item")){
                GenericDao<ProductCategoryEntity> productDao = new GenericDao(sessionFactory);
                ProductCategoryEntity entityProduct = new ProductCategoryEntity();
                productDao.create(entityProduct);
                entityProduct.setProdId(list.item(i).getFirstChild().getNodeValue());

                entityProduct.setCategoryId(categoryDaoMap.get((returnTextValueOfNode(list.item(i).getParentNode()))));
            }
        }

    }

    String returnTextValueOfNode(Node node){
        String value;
        if (node == null){
            value = "none";
            return value;
        }
        else if (node.getFirstChild() == null){
            value = "none";
            return value;
        }
        else value = node.getFirstChild().getNodeValue().trim();
        return value;
    }

    String returnTagOfNode(Node node){
        String value;
        if (node == null){
            value = "";
            return  value;
        }
        else value = node.getNodeName().trim();
        return value;
    }

}
