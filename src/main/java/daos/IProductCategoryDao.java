package daos;

import entities.ProductCategoryEntity;
import entities.ProductCategoryEntityPK;

public interface IProductCategoryDao extends IGenericDao<ProductCategoryEntity> {

    ProductCategoryEntity findOne(ProductCategoryEntityPK productCategoryPK);
}
