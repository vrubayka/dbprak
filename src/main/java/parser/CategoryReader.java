package parser;

import daos.GenericDao;
import daos.ProductDao;
import entities.CategoryEntity;
import entities.ProductCategoryEntity;
import entities.ProductEntity;
import entities.ReviewEntity;
import logging.exceptions.ShopReaderExceptions;
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
        this.doc = doc;
        this.sessionFactory = sessionFactory;
    }

    public void parseCategories(NodeList list, SessionFactory sessionFactory) throws ShopReaderExceptions {
        for (int i = 0; i < list.getLength(); i++) {

            if ((XmlParser.returnTagOfNode(list.item(i))).equals("category")) {

                GenericDao<CategoryEntity> daoCat = new GenericDao<>(sessionFactory);
                CategoryEntity entityCat = new CategoryEntity();
                entityCat.setCategoryName(XmlParser.returnTextValueOfNode(list.item(i)));

                if (XmlParser.returnTagOfNode(list.item(i).getParentNode()).equals("category")) {
                    entityCat.setSuperCategory(categoryDaoMap.get(XmlParser.returnTextValueOfNode(list.item(i).getParentNode())));

                    daoCat.create(entityCat);
                    categoryDaoMap.put(XmlParser.returnTextValueOfNode(list.item(i)), entityCat.getCategoryId());
                    parseCategories(list.item(i).getChildNodes(), sessionFactory);

                } else if ((XmlParser.returnTagOfNode(list.item(i))).equals("item")) {
                    try {
                        GenericDao<ProductCategoryEntity> productDao = new GenericDao(sessionFactory);
                        ProductCategoryEntity entityProductCategory = new ProductCategoryEntity();
                        entityProductCategory.setProdId(XmlParser.returnTextValueOfNode(list.item(i)));
                        entityProductCategory.setCategoryId(categoryDaoMap.
                                get(XmlParser.returnTextValueOfNode(list.item(i).getParentNode())));
                        productDao.create(entityProductCategory);
                    } catch (jakarta.persistence.PersistenceException e) {
                        System.out.println("Item " + XmlParser.returnTextValueOfNode(list.item(i)) + " missing");
                    }
                }
            }

        }
    }
}
