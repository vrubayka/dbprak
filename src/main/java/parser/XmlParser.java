package parser;

import entities.AddressEntity;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class XmlParser implements Reader{

    @Override
    public void readFile(String filePath) {
        File inputFile = new File(filePath);
        Document doc = getNormalizedDocument(inputFile);

        String rootElement = doc.getDocumentElement().getNodeName();
        switch (rootElement) {
            case "shop":
                System.out.println("Reading shop...");
                ShopReader shopReader = new ShopReader(doc);

                System.out.println("Finished reading.");
        }
    }

    private void readShopXml(Document doc) {
        Element root = doc.getDocumentElement();
        NamedNodeMap attributeMap = root.getAttributes();
        AddressEntity shopAddress = new AddressEntity();
        shopAddress.setCity(attributeMap.getNamedItem("name").getNodeValue());
        shopAddress.setPostcode(attributeMap.getNamedItem(""));
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
