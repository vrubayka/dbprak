package daos;

import entities.ProductEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.List;

public class ProductDao extends GenericDao<ProductEntity> {

    public ProductDao(SessionFactory sessionFactory) {
        super(ProductEntity.class, sessionFactory);
    }

    public ProductEntity findOne(String prodId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        ProductEntity product = session.get(this.getDaoClass(), prodId);
        tx.commit();

        return product;
    }

    public List<ProductEntity> findByPattern(String pattern) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<ProductEntity> query = session.createSelectionQuery(
                "FROM ProductEntity p WHERE p.prodName like :pattern escape '\\'", ProductEntity.class)
                .setParameter("pattern", pattern);
        List<ProductEntity> productEntityList = query.getResultList();
        tx.commit();
        return productEntityList;
    }
}
