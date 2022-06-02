package daos;

import entities.SimilarProductsEntity;
import entities.SimilarProductsEntityPK;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class SimilarProductDao extends GenericDao<SimilarProductsEntity> implements ISimilarProductDao {


    public SimilarProductDao(Class<SimilarProductsEntity> daoClass, SessionFactory sessionFactory) {
        super(SimilarProductsEntity.class, sessionFactory);
    }

    @Override
    public SimilarProductsEntity findOne(SimilarProductsEntityPK similarProductsPK) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SimilarProductsEntity similarProduct = session.get(SimilarProductsEntity.class, similarProductsPK);
        tx.commit();

        return similarProduct;
    }
}
