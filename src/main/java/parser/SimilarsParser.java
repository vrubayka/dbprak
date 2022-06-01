package parser;

import daos.GenericDao;
import entities.ProductEntity;
import entities.SimilarProductsEntity;
import org.hibernate.SessionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SimilarsParser {


    public SimilarsParser() {}

    public void readSimilarProducts(NodeList nodeList, SessionFactory sessionFactory) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals("sim_product")) {
                String asin = nodeList.item(i).getParentNode().getParentNode().getAttributes()
                        .getNamedItem("asin").getNodeValue();
                SimilarProductsEntity simProduct = new SimilarProductsEntity();
                GenericDao<SimilarProductsEntity> simDao = new GenericDao<>(sessionFactory);
                simProduct.setProdId(asin);
                for (Node childNode = nodeList.item(i).getFirstChild(); childNode != null;
                     childNode = childNode.getNextSibling()) {
                    if (childNode.getNodeType() == Node.ELEMENT_NODE &&
                            childNode.getNodeName().equals("sim_product")) {
                        for (Node similarAttrNode = childNode.getFirstChild(); similarAttrNode != null;
                             similarAttrNode = similarAttrNode.getNextSibling()) {
                            if (similarAttrNode.getNodeName().equals("asin")) {
                                simProduct.setSimilarProdId(similarAttrNode.getNodeValue());
                            }
                        }
                    }
                }
                if (simProduct.getProdId() == null) {
                    simProduct.setProdId("");
                    simDao.create(simProduct);
                }
            }
        }
    }
}
