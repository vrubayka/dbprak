package daos;

import entities.ProductEntity;
import entities.SimilarProductsEntity;
import entities.SimilarProductsEntityPK;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.List;

public class SimilarProductDao extends GenericDao<SimilarProductsEntity> implements ISimilarProductDao {


    public SimilarProductDao(SessionFactory sessionFactory) {
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

    public List<SimilarProductsEntity> findSimilarProducts(String id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<SimilarProductsEntity> query = session.createSelectionQuery(
                "SELECT * FROM SimilarProductsEntity WHERE prodId = :id", SimilarProductsEntity.class)
                .setParameter("id", id);
        List<SimilarProductsEntity> liste = query.getResultList();
        tx.commit();
        return liste;
    }
}
