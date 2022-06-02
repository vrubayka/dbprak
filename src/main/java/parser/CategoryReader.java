package parser;

import daos.GenericDao;
import entities.*;
import logging.ReadLog;
import logging.ReadingError;
import logging.exceptions.MissingProductNameException;
import org.hibernate.SessionFactory;
import org.w3c.dom.Document;
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

    /**
     * Parses all categories recursively assigning super-categories
     * when applicable
     * @param list - list of all nodes from XML - categories and items
     * @param sessionFactory - factory to create sessions in DAOs
     * @throws MissingProductNameException - if product Name is missing
     */
    public void parseCategories(NodeList list, SessionFactory sessionFactory) throws MissingProductNameException {
        for (int i = 0; i < list.getLength(); i++) {

            if ((XmlParser.returnTagOfNode(list.item(i))).equals("category")) {

                GenericDao<CategoryEntity> daoCat = new GenericDao<>(sessionFactory);
                CategoryEntity entityCat = new CategoryEntity();
                entityCat.setCategoryName(XmlParser.returnTextValueOfNode(list.item(i)));

                if (XmlParser.returnTagOfNode(list.item(i).getParentNode()).equals("category")) {
                    entityCat.setSuperCategory(categoryDaoMap.get(XmlParser.returnTextValueOfNode(list.item(i).getParentNode())));
                    if (isNewCategory(entityCat)) {
                        daoCat.create(entityCat);
                        categoryDaoMap.put(XmlParser.returnTextValueOfNode(list.item(i)), entityCat.getCategoryId());
                    } else {
                        ReadLog.addDuplicate(new ReadingError("Category", entityCat.getCategoryName(), "Duplicate",
                                "Category already in Database."));
                    }
                }
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


    /**
     * Checks if category not yet in the database
     * @param category - category to be checked
     * @return true if not in the database, false otherwise
     */
    private boolean isNewCategory(CategoryEntity category) {
        GenericDao<CategoryEntity> catDao = new GenericDao<>(sessionFactory);
        ProductCategoryEntityPK categoryPK = new ProductCategoryEntityPK();
        categoryPK.setCategoryId(category.getCategoryId());
        if (catDao.findOne(categoryPK.getCategoryId()) == null) {
            return true;
        }
        return false;
    }
}
