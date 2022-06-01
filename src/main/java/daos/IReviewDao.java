package daos;

import entities.ReviewEntity;
import entities.ReviewEntityPK;

public interface IReviewDao extends IGenericDao<ReviewEntity> {

    ReviewEntity findOne(ReviewEntityPK reviewPK);
}
