package daos;

import entities.ReviewEntity;
import entities.ReviewEntityPK;

import java.util.List;

public interface IReviewDao extends IGenericDao<ReviewEntity> {

    ReviewEntity findOne(ReviewEntityPK reviewPK);
    List<ReviewEntity> findByProdId(String prodId);
}
