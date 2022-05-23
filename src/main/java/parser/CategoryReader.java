package parser;

import daos.GenericDao;
import entities.CategoryEntity;
import entities.ProductCategoryEntity;
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
            if ((returnTagOfNode(list.item(i))).equals("category")) {

                GenericDao<CategoryEntity> daoCat = new GenericDao<>(sessionFactory);
                CategoryEntity entityCat = new CategoryEntity();

                entityCat.setCategoryName(returnTextValueOfNode(list.item(i)));
                daoCat.create(entityCat);
                categoryDaoMap.put(returnTextValueOfNode(list.item(i)), entityCat.getCategoryId());

                if (returnTagOfNode(list.item(i).getParentNode()).equals("category")){
                    entityCat.setSuperCategory(categoryDaoMap.get(returnTextValueOfNode(list.item(i))));
                }
                parseCategories(list.item(i).getChildNodes(), sessionFactory);

            }
            else if ((returnTagOfNode(list.item(i))).equals("item")){
                GenericDao<ProductCategoryEntity> productDao = new GenericDao(sessionFactory);
                ProductCategoryEntity entityProduct = new ProductCategoryEntity();
                productDao.create(entityProduct);
                entityProduct.setProdId(list.item(i).getFirstChild().getNodeValue());

                entityProduct.setCategoryId(categoryDaoMap.get((returnTextValueOfNode(list.item(i).getParentNode()))));
            }
        }
        System.out.println("Fertig");
    }

    String returnTextValueOfNode(Node node){
        String value;
        if (node == null){
            value = "none";
            return value;
        }
        else if (node.getFirstChild() == null){
            value = "none";
            return value;
        }
        else value = node.getFirstChild().getNodeValue().trim();
        return value;
    }

    String returnTagOfNode(Node node){
        String value;
        if (node == null){
            value = "";
            return value;
        }
        else value = node.getNodeName().trim();
        return value;
    }
}
