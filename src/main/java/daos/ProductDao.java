package daos;

import entities.ProductEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ProductDao extends GenericDao<ProductEntity> {

    public ProductDao(SessionFactory sessionFactory) {
        super(ProductEntity.class, sessionFactory);
    }

    public ProductEntity findOne(String prodId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        ProductEntity productEntity = session.get(this.getDaoClass(), prodId);
        tx.commit();
        return productEntity;
    }
}
