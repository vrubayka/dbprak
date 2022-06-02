package parser;

import org.hibernate.SessionFactory;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;
import java.util.HashMap;

public class XmlParser implements Reader{
    /**
     * creates a Document-type file from a shop-XML for purposes of parsing
     * and calls the parser
     * @param filePath - path to the XML
     * @param sessionFactory - factory to create sessions in DAOs
     */
    @Override
    public void readFile(String filePath, SessionFactory sessionFactory) {
        File inputFile = new File(filePath);
        Document doc = getNormalizedDocument(inputFile);
        String rootElement = doc.getDocumentElement().getNodeName();


        if (rootElement.equals("shop")){
                if (doc.getDocumentElement().getAttribute("name").equals("Dresden")){
                    System.out.println("Reading Dresden shop...");
                    DresdenReader dresdenReader = new DresdenReader(doc, sessionFactory);
                    dresdenReader.readStoreXml();
                }
                else {
                    System.out.println("Reading Leipzig shop...");
                    LeipzigReader leipzigReader = new LeipzigReader(doc, sessionFactory);
                    leipzigReader.readStoreXml();
                }


                System.out.println("Finished reading shop Xml.");

        }

    }

    /**
     * a separate method to convert the Categories-XML to a
     * Document datatype for further parsing
     * @param pathfile - path to the XML
     * @param sessionFactory - factory to create sessions in DAOs
     */
    public void readCategories(String pathfile, SessionFactory sessionFactory){
        File inputFile = new File(pathfile);
        Document doc = getNormalizedDocument(inputFile);
        CategoryReader cr = new CategoryReader(doc, sessionFactory);
        System.out.println("Parsing categories:");
        cr.parseCategories(doc.getDocumentElement().getChildNodes(), 0,  sessionFactory);
        System.out.println("Finished reading categories");
    }

    /**
     * Prepares the shop-XMLs for the process
     * of parsing of similar elements
     * @param pathfile - path to the XML
     * @param sessionFactory - factory to create sessions in DAOs
     */
    public void readSimilars(String pathfile, SessionFactory sessionFactory){
        File inputFile = new File(pathfile);
        Document doc = getNormalizedDocument(inputFile);
        Node root = doc.getDocumentElement();
        SimilarsParser sr = new SimilarsParser();
        System.out.println("Parsing similars:");
        sr.readItems(root, sessionFactory);
        System.out.println("Finished parsing similars");
    }

    /**
     * reduces redundancies in the XML
     * @param inputFile the XML to be cleared from redundancies
     * @return a normalized document
     */
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

    /**
     * extracts the text value from a Node
     * @param node - the node from which the text value is extracted
     * @return - the desired text value
     */
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
        else value = node.getFirstChild().getNodeValue();
        return value;
    }

    /**
     * extracts the name of the Node-tag
     * @param node - the node the tag of which need to be extracted
     * @return the text of the tag
     */
    static String returnTagOfNode(Node node){
        String value;
        if (node == null){
            value = "";
            return  value;
        }
        else value = node.getNodeName();
        return value;
    }

}
