package daos;

import entities.ProductCategoryEntity;
import entities.ProductCategoryEntityPK;

import java.util.List;

public interface IProductCategoryDao extends IGenericDao<ProductCategoryEntity> {

    public ProductCategoryEntity findOne(ProductCategoryEntityPK productCategoryPK);

    public List<ProductCategoryEntity> findByCategoryId(Long id);
}
