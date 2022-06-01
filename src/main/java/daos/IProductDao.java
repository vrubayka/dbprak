package daos;

import entities.ProductEntity;

public interface IProductDao extends IGenericDao<ProductDao> {

    ProductEntity findOne(String prodId);
}
