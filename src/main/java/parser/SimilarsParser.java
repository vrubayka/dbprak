package parser;

import daos.ProductDao;
import daos.SimilarProductDao;
import entities.SimilarProductsEntity;
import entities.SimilarProductsEntityPK;
import logging.ReadLog;
import logging.ReadingError;
import org.hibernate.SessionFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

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
        String shopName = root.getAttributes().getNamedItem("name").getNodeValue();

        for (Node rootChildNode = root.getFirstChild(); rootChildNode != null;
             rootChildNode = rootChildNode.getNextSibling()) {

            if (rootChildNode.getNodeType() == Node.ELEMENT_NODE && rootChildNode.getNodeName().equals("item")) {

                // check if asin node exists
                Node asinNode = rootChildNode.getAttributes().getNamedItem("asin");
                if (asinNode == null || asinNode.getNodeValue().equals("")) {
                    ReadLog.addError(new ReadingError("Product", null, "asin",
                                                      "Product has no asin."));

                } else {
                    String prodId = asinNode.getNodeValue();
                    if (referencedProdIdExists(prodId, sessionFactory)) {
                        for (Node itemElementNode = rootChildNode.getFirstChild();
                             itemElementNode != null;
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
    }

    private void readSimilarsLeipzig(Node similarsNode, String prodId, SessionFactory sessionFactory) {
        for (Node simNode = similarsNode.getFirstChild(); simNode != null;
             simNode = simNode.getNextSibling()) {

            if (simNode.getNodeType() == Node.ELEMENT_NODE && simNode.getNodeName().equals("sim_product")) {
                readProductLeipzig(simNode, prodId, sessionFactory);
            }

        }
    }

    private void readSimilarsDresden(Node similarsNode, String prodId, SessionFactory sessionFactory) {
        for (Node simNode = similarsNode.getFirstChild(); simNode != null;
             simNode = simNode.getNextSibling()) {

            if (simNode.getNodeType() == Node.ELEMENT_NODE && simNode.getNodeName().equals("item")) {
                readProductDresden(simNode, prodId, sessionFactory);
            }

        }
    }

    private void readProductLeipzig(Node simProdNode, String prodId, SessionFactory sessionFactory) {
        String simProdId;
        for (Node productNode = simProdNode.getFirstChild(); productNode != null;
             productNode = productNode.getNextSibling()) {

            if (productNode.getNodeType() == Node.ELEMENT_NODE && productNode.getNodeName().equals("asin")) {
                simProdId = productNode.getFirstChild().getNodeValue();

                if (simProdId != null && referencedProdIdExists(simProdId, sessionFactory)) {
                    SimilarProductsEntity similarProduct = new SimilarProductsEntity();
                    similarProduct.setProdId(prodId);
                    similarProduct.setSimilarProdId(simProdId);

                    if (isNewSimilarProduct(similarProduct, sessionFactory)) {
                        insertSimilarProduct(similarProduct, sessionFactory);
                    }
                }
            }
        }
    }

    private void readProductDresden(Node simProdNode, String prodId, SessionFactory sessionFactory) {
        NamedNodeMap simProdAttributes = simProdNode.getAttributes();
        String simProdId = simProdAttributes.getNamedItem("asin").getNodeValue();

        if (simProdId != null) {
            SimilarProductsEntity similarProduct = new SimilarProductsEntity();
            similarProduct.setProdId(prodId);
            similarProduct.setSimilarProdId(simProdId);

            if (isNewSimilarProduct(similarProduct, sessionFactory)) {
                insertSimilarProduct(similarProduct, sessionFactory);
            }
        }
    }

    private boolean isNewSimilarProduct(SimilarProductsEntity similarProduct, SessionFactory sessionFactory) {
        SimilarProductsEntityPK similarProductPK = new SimilarProductsEntityPK();
        similarProductPK.setProdId(similarProduct.getProdId());
        similarProductPK.setSimilarProdId(similarProduct.getSimilarProdId());

        SimilarProductDao similarProductDao = new SimilarProductDao(sessionFactory);

        if (similarProductDao.findOne(similarProductPK) == null) {
            return true;
        }
        return false;
    }

    private void insertSimilarProduct(SimilarProductsEntity similarProduct, SessionFactory sessionFactory) {
        SimilarProductDao similarProductDao = new SimilarProductDao(sessionFactory);
        similarProductDao.create(similarProduct);
    }

    private boolean referencedProdIdExists(String prodId, SessionFactory sessionFactory) {
        ProductDao productDao = new ProductDao(sessionFactory);
        if(productDao.findOne(prodId) != null) {
            return true;
        }
        return false;
    }
}



