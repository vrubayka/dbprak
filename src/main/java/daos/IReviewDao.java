package daos;

import entities.ReviewEntity;

import java.util.List;

public interface IReviewDao extends IGenericDao<ReviewEntity> {

    List<ReviewEntity> findByProdId(String prodId);
}
