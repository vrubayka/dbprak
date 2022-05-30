package parser;

import daos.GenericDao;
import entities.CategoryEntity;
import entities.ProductCategoryEntity;
import entities.ProductEntity;
import entities.ReviewEntity;
import org.hibernate.SessionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

public class CategoryReader {
    private final Document doc;
    private final SessionFactory sessionFactory;

    HashMap<String, Long> categoryDaoMap = new HashMap<>();

    public CategoryReader(Document doc, SessionFactory sessionFactory) {
        this.doc            = doc;
        this.sessionFactory = sessionFactory;
    }

    public void parseCategories(NodeList list, SessionFactory sessionFactory) {
        System.out.println("Reading categories");
        for (int i = 0; i < list.getLength(); i++) {

            if ((XmlParser.returnTagOfNode(list.item(i))).equals("category")) {

                GenericDao<CategoryEntity> daoCat = new GenericDao<>(sessionFactory);
                CategoryEntity entityCat = new CategoryEntity();
                entityCat.setCategoryName(list.item(i).getFirstChild().getNodeValue().trim());

                if (list.item(i).getParentNode().getNodeName().equals("category")) {
                    entityCat.setSuperCategory(categoryDaoMap.get(list.item(i).getParentNode().getFirstChild().getNodeValue().trim()));
                }
                daoCat.create(entityCat);
                categoryDaoMap.put(list.item(i).getFirstChild().getNodeValue().trim(), entityCat.getCategoryId());
                //System.out.println("MAP INHALT: " + categoryDaoMap);
                parseCategories(list.item(i).getChildNodes(), sessionFactory);

            }
            else if ((XmlParser.returnTagOfNode(list.item(i))).equals("item")){
                /*try {


                    ProductEntity product = new ProductEntity();
                    product.setProdId(list.item(i).getFirstChild().getNodeValue());
                    product.setProdName("platzhalter");
                    product.setRating(2.342);
                    GenericDao<ProductEntity> productEntityDao = new GenericDao<>(sessionFactory);
                    GenericDao<ReviewEntity> reviewEntityDao = new GenericDao<>(sessionFactory);
                    productEntityDao.create(product);
                    System.out.println("Product: " + list.item(i).getParentNode().getFirstChild().getNodeValue());
                } catch (jakarta.persistence.PersistenceException e){
                    System.out.println("Dublicate Item declined: " + list.item(i).getFirstChild().getNodeValue());
                }*/
                try {
                    GenericDao<ProductCategoryEntity> productDao = new GenericDao(sessionFactory);
                    ProductCategoryEntity entityProductCategory = new ProductCategoryEntity();
                    entityProductCategory.setProdId(list.item(i).getFirstChild().getNodeValue());
                    entityProductCategory.setCategoryId(categoryDaoMap.get(list.item(i).getParentNode().getFirstChild().getNodeValue().trim()));
                    productDao.create(entityProductCategory);
                } catch (jakarta.persistence.PersistenceException e){
                    System.out.println("Item " + list.item(i).getFirstChild().getNodeValue() + " missing");

                }
            }
        }
        System.out.println("Finished reading categories");
    }
}
