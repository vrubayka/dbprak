package daos;

import entities.ProductEntity;

import java.util.List;

public interface IProductDao extends IGenericDao<ProductDao> {

    ProductEntity findOne(String prodId);

    List<ProductEntity> findByPattern(String pattern);

    List<ProductEntity> findTopProducts(int k);
}
