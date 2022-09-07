package daos;

import entities.CategoryEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.List;

public class CategoryDao extends GenericDao<CategoryEntity> implements ICategoryDao {

    public CategoryDao(SessionFactory sessionFactory) {
        super(CategoryEntity.class, sessionFactory);
    }

    @Override
    public List<CategoryEntity> findBySuperCategory(Long superId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<CategoryEntity> query;
        if(superId == null) {
            query = session.createSelectionQuery("FROM CategoryEntity c WHERE superCategory IS NULL",
                                                 CategoryEntity.class);
        } else {
            query = session.createSelectionQuery(
                            "FROM CategoryEntity c WHERE superCategory = :superId",
                            CategoryEntity.class)
                    .setParameter("superId", superId.toString());
        }
        List<CategoryEntity> categoryEntityList = query.getResultList();
        tx.commit();
        return categoryEntityList;
    }
}
