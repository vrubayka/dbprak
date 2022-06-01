package daos;

import entities.ProductEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ProductDao extends GenericDao<ProductEntity> {

    public ProductDao(SessionFactory sessionFactory) {
        super(ProductEntity.class, sessionFactory);
    }

    public ProductEntity findOne(String prodId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(this.getDaoClass(), prodId);
    }
}
