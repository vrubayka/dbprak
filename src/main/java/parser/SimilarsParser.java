package parser;

import daos.GenericDao;
import entities.ProductEntity;
import entities.SimilarProductsEntity;
import org.hibernate.SessionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SimilarsParser {


    public SimilarsParser() {
    }

    /**
     * Parses all the items that contain similar items
     * @param root - root element of the xml
     * @param sessionFactory - factory to create sessions in DAOs
     */
    public void readItems(Node root, SessionFactory sessionFactory) {
        SimilarProductsEntity simProduct = new SimilarProductsEntity();
        String asin = "";
        GenericDao<SimilarProductsEntity> simDao = new GenericDao<>(sessionFactory);

        for (Node itemNode = root.getFirstChild(); itemNode != null;  //<item>
             itemNode = itemNode.getNextSibling()) {

            for (Node itemAttrNode = itemNode.getFirstChild(); itemAttrNode != null; //<price>, <title>...
                 itemAttrNode = itemAttrNode.getNextSibling()) {

                if (itemAttrNode.getNodeName().equals("similars")) {

                    for (Node simNode = itemAttrNode.getFirstChild(); simNode != null;
                         simNode = simNode.getNextSibling()) {

                        if (simNode.getNodeValue() != null) {
                            asin = itemNode.getAttributes()
                                    .getNamedItem("asin").getNodeValue();
                            simProduct.setProdId(asin);
                        }

                        for (Node simAttrNode = simNode.getFirstChild(); simAttrNode != null;
                             simAttrNode = simAttrNode.getNextSibling()) {
                            if (simAttrNode.getNodeType() == Node.ELEMENT_NODE &&
                                    simAttrNode.getNodeName().equals("asin")) {
                                simProduct.setSimilarProdId(simAttrNode.getFirstChild().getNodeValue());
                                if (simProduct.getProdId() != null) {
                                    simDao.create(simProduct);

                                }
                            }
                        }

                    }
                }
            }
        }
    }
}



