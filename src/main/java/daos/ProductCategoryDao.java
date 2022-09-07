package daos;

import entities.ProductCategoryEntity;
import entities.ProductCategoryEntityPK;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.List;

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

    public List<ProductCategoryEntity> findByCategoryId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<ProductCategoryEntity> query = session.createSelectionQuery(
                "FROM ProductCategoryEntity pc WHERE pc.categoryId = :id",
                ProductCategoryEntity.class)
                .setParameter("id", id.toString());
        List<ProductCategoryEntity> productCategoryEntities = query.getResultList();
        tx.commit();

        return productCategoryEntities;
    }
}
