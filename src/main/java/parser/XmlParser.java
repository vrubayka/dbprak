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

            case "categories":
                System.out.println("Reading categories...");
                CategoryReader categoryReader = new CategoryReader(doc, sessionFactory);
                categoryReader.parseCategories(doc.getDocumentElement().getChildNodes(), sessionFactory);
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

}
