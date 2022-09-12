package daos;

import entities.SimilarProductsEntity;
import entities.SimilarProductsEntityPK;

import java.util.List;

public interface ISimilarProductDao extends IGenericDao<SimilarProductsEntity> {

    SimilarProductsEntity findOne(SimilarProductsEntityPK similarProductsPK);
    List<SimilarProductsEntity> findSimilarProducts(String id);
}
