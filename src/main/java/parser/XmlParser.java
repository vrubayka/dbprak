package parser;

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
                LeipzigReader leipzigReader = new LeipzigReader(doc, sessionFactory);
                leipzigReader.readStoreXml();

                System.out.println("Finished reading shop Xml.");

            //ToDo: uncomment
            case "categories":
                System.out.println("Reading categories...");
                CategoryReader categoryReader = new CategoryReader(doc, sessionFactory);
                categoryReader.parseCategories(doc.getDocumentElement().getChildNodes(), sessionFactory);

                System.out.println("Finished reading categories Xml.");


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

    static String returnTextValueOfNode(Node node){
        String value;
        if (node == null){
            value = "";
            return value;
        }
        else if (node.getFirstChild() == null){
            value = "";
            return value;
        }
        else value = node.getFirstChild().getNodeValue().trim();
        return value;
    }

    static String returnTagOfNode(Node node){
        String value;
        if (node == null){
            value = "";
            return  value;
        }
        else value = node.getNodeName().trim();
        return value;
    }

}
