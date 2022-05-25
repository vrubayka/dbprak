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
        for (int i = 0; i < list.getLength(); i++) {

            if ((XmlParser.returnTagOfNode(list.item(i))).equals("category")) {

                GenericDao<CategoryEntity> daoCat = new GenericDao<>(sessionFactory);
                CategoryEntity entityCat = new CategoryEntity();

                entityCat.setCategoryName(XmlParser.returnTextValueOfNode(list.item(i)));
                daoCat.create(entityCat);
                categoryDaoMap.put(XmlParser.returnTextValueOfNode(list.item(i)), entityCat.getCategoryId());

                if (XmlParser.returnTagOfNode(list.item(i).getParentNode()).equals("category")){
                    entityCat.setSuperCategory(categoryDaoMap.get(XmlParser.returnTextValueOfNode(list.item(i))));
                }
                parseCategories(list.item(i).getChildNodes(), sessionFactory);

            }
            else if ((XmlParser.returnTagOfNode(list.item(i))).equals("item")){

                ProductEntity product = new ProductEntity();
                product.setProdId(list.item(i).getFirstChild().getNodeValue());
                product.setProdName("platzhalter");
                product.setRating(2.342);
                GenericDao<ProductEntity> productEntityDao = new GenericDao<>(sessionFactory);
                GenericDao<ReviewEntity> reviewEntityDao = new GenericDao<>(sessionFactory);
                productEntityDao.create(product);

                GenericDao<ProductCategoryEntity> productDao = new GenericDao(sessionFactory);
                ProductCategoryEntity entityProduct = new ProductCategoryEntity();
                productDao.create(entityProduct);
                entityProduct.setProdId(list.item(i).getFirstChild().getNodeValue());

                entityProduct.setCategoryId(categoryDaoMap.get((XmlParser.returnTextValueOfNode(list.item(i).getParentNode()))));
            }
        }
    }
}
