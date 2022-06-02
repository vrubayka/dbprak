package parser;

import daos.GenericDao;
import daos.SimilarProductDao;
import entities.ProductEntity;
import entities.SimilarProductsEntity;
import entities.SimilarProductsEntityPK;
import logging.ReadLog;
import org.hibernate.SessionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SimilarsParser {


    public SimilarsParser() {
    }

    /**
     * Parses all the items that contain similar items
     *
     * @param root           - root element of the xml
     * @param sessionFactory - factory to create sessions in DAOs
     */
    public void readItems(Node root, SessionFactory sessionFactory) {
        SimilarProductsEntity simProduct = new SimilarProductsEntity();
        GenericDao<SimilarProductsEntity> simDao = new GenericDao<>(SimilarProductsEntity.class, sessionFactory);
        String shopName = root.getAttributes().getNamedItem("name").getNodeValue();

        for (Node rootChildNode = root.getFirstChild(); rootChildNode != null;  //<item>
             rootChildNode = rootChildNode.getNextSibling()) {

            if (rootChildNode.getNodeType() == Node.ELEMENT_NODE && rootChildNode.getNodeName().equals("item")) {
                String prodId = rootChildNode.getAttributes().getNamedItem("asin").getNodeValue();

                if (prodId != null) {
                    for (Node itemElementNode = rootChildNode.getFirstChild();
                         itemElementNode != null;  // <price> <title>
                         itemElementNode = itemElementNode.getNextSibling()) {

                        if (itemElementNode.getNodeName().equals("similars")) {
                            if (shopName.equals("Leipzig")) {
                                readSimilarsLeipzig(itemElementNode, prodId, sessionFactory);
                            } else if (shopName.equals("Dresden")) {
                                readSimilarsDresden(itemElementNode, prodId, sessionFactory);
                            }

                        }
                    }
                }
            }
        }
    }

    private void readSimilarsLeipzig(Node similarsNode, String prodId, SessionFactory sessionFactory) {

        for (Node simNode = similarsNode.getFirstChild(); simNode != null;
             simNode = simNode.getNextSibling()) {

            if (simNode.getNodeType() == Node.ELEMENT_NODE && simNode.getNodeName().equals("sim_product")) {
                readProductLeipzig(simNode, prodId, sessionFactory);
            }

        }
    }

    private void readProductLeipzig(Node simProdNode, String prodId, SessionFactory sessionFactory) {
        String simProdId;
        for (Node productNode = simProdNode.getFirstChild(); productNode != null;
             productNode = productNode.getNextSibling()) {

            if (productNode.getNodeType() == Node.ELEMENT_NODE && productNode.getNodeName().equals("asin")) {
                simProdId = productNode.getFirstChild().getNodeValue();

                if(simProdId != null) {
                    SimilarProductsEntity similarProduct = new SimilarProductsEntity();
                    similarProduct.setProdId(prodId);
                    similarProduct.setSimilarProdId(simProdId);

                    if(isNewSimilarProduct(similarProduct, sessionFactory)) {
                        insertSimilarProduct(similarProduct);
                    }
                }
            }
        }
    }

    private boolean isNewSimilarProduct(SimilarProductsEntity similarProduct, SessionFactory sessionFactory) {
        SimilarProductsEntityPK similarProductPK = new SimilarProductsEntityPK();
        similarProductPK.setProdId(similarProduct.getProdId());
        similarProductPK.setSimilarProdId(similarProduct.getSimilarProdId());

        SimilarProductDao similarProductDao = new SimilarProductDao(SimilarProductsEntity.class, sessionFactory);

        if(similarProductDao.findOne(similarProductPK) == null) {
            return true;
        }
        return false;
    }
}



