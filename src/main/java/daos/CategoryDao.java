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

    public List<CategoryEntity> findBySuperCategory(Long superId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<CategoryEntity> query;
        if (superId == null) {
            query = session.createSelectionQuery("FROM CategoryEntity c WHERE c.superCategory IS NULL",
                                                 CategoryEntity.class);
        } else {
            query = session.createSelectionQuery(
                            "FROM CategoryEntity c WHERE c.superCategory = :superId",
                            CategoryEntity.class)
                    .setParameter("superId", superId.toString());
        }
        List<CategoryEntity> categoryEntityList = query.getResultList();
        tx.commit();
        return categoryEntityList;
    }

    public CategoryEntity findByNameAndSuperCategory(String name, Long superId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<CategoryEntity> query;
        if (superId == null) {
            query = session.createSelectionQuery(
                            "FROM CategoryEntity c WHERE c.categoryName = :name AND c.superCategory IS NULL",
                            CategoryEntity.class)
                    .setParameter("name", name);
        } else {
            query = session.createSelectionQuery(
                    "FROM CategoryEntity c WHERE c.categoryName = :name AND c.superCategory = :superId",
                    CategoryEntity.class)
                    .setParameter("name", name)
                    .setParameter("superId", superId.toString());
        }
        CategoryEntity category = query.getSingleResultOrNull();
        tx.commit();

        return category;
    }
}
