package daos;

import entities.ProductCategoryEntity;
import entities.ProductCategoryEntityPK;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ProductCategoryDao extends GenericDao<ProductCategoryEntity> implements IProductCategoryDao {

    public ProductCategoryDao(SessionFactory sessionFactory) {
        super(ProductCategoryEntity.class, sessionFactory);
    }

    @Override
    public ProductCategoryEntity findOne(ProductCategoryEntityPK productCategoryPK) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        ProductCategoryEntity productCategory = session.get(ProductCategoryEntity.class, productCategoryPK);
        tx.commit();

        return productCategory;
    }
}
