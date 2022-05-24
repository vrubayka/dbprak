package parser;


import org.hibernate.SessionFactory;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.parsers.ParserConfigurationException;

import java.io.File;

import java.io.IOException;


public class XmlParser implements Reader {

    @Override
    public void readFile(String filePath, SessionFactory sessionFactory) {
        File inputFile = new File(filePath);
        Document doc = getNormalizedDocument(inputFile);
        String rootElement = doc.getDocumentElement().getNodeName();

        if (rootElement.equals("Dresden")) {

            System.out.println("Reading the Dresden shop...");
            StoreReader reader = new StoreReader(doc, sessionFactory);
            reader.readStoreXml();

            System.out.println("Finished reading the Dresden shop.");

            System.out.println("Finished reading shop Xml.");

        } else if (rootElement.equals("Leipzig")) {

            System.out.println("Reading the Leipzig shop...");
            StoreReader reader = new StoreReader(doc, sessionFactory);
            reader.readStoreXml();

            System.out.println("Finished reading the Leipzig shop.");

        } else {
            System.out.println("Reading the Categories...");
            CategoryReader categoryReader = new CategoryReader(doc, sessionFactory);
            categoryReader.parseCategories(doc.getDocumentElement().getChildNodes(),
                                           sessionFactory);

            System.out.println("Finished reading the Categories XML.");
        }
    }


    private Document getNormalizedDocument(File inputFile) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document doc = documentBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
