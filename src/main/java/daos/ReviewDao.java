package daos;

import entities.ReviewEntity;
import entities.ReviewEntityPK;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.List;

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

    @Override
    public List<ReviewEntity> findByProdId(String prodId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<ReviewEntity> query = session.createSelectionQuery(
                "SELECT r FROM ReviewEntity r WHERE r.prodId = :prodId", ReviewEntity.class);

        query.setParameter("prodId", prodId);
        List<ReviewEntity> reviewList = query.getResultList();
        tx.commit();

        return reviewList;
    }

    public List<ReviewEntity> findAggregateRatingOfUser(Double minRating){
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<ReviewEntity> query = session.createSelectionQuery(
                "SELECT DISTINCT username, AVG(rating) FROM ReviewEntity GROUP BY username " +
                        "having AVG(rating) < :minRating  ", ReviewEntity.class);
        query.setParameter("minRating", minRating);
        List<ReviewEntity> reviewsOfUser = query.getResultList();
        tx.commit();

        return reviewsOfUser;
    }

}
