package daos;

import entities.ReviewEntity;
import entities.ReviewEntityPK;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ReviewDao extends GenericDao<ReviewEntity> implements IReviewDao {

    public ReviewDao(SessionFactory sessionFactory) {
        super(ReviewEntity.class, sessionFactory);
    }

    @Override
    public ReviewEntity findOne(ReviewEntityPK reviewPK) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        ReviewEntity review = session.get(ReviewEntity.class, reviewPK);
        tx.commit();

        return review;
    }
}
