package daos;

import entities.SimilarProductsEntity;
import entities.SimilarProductsEntityPK;

public interface ISimilarProductDao extends IGenericDao<SimilarProductsEntity> {

    SimilarProductsEntity findOne(SimilarProductsEntityPK similarProductsPK);
}
