package middle;

import entities.InventoryEntity;
import entities.ProductEntity;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

public interface IMenuMapper {

    void init();

    void finish();

    ProductEntity getProduct(String id);

    List<ProductEntity> getProducts(String pattern);

    CategoryNode getCategoryTree();

    List<ProductEntity> getProductsByCategoryPath(String path);

    List<ProductEntity> getTopProducts();

    List<ProductEntity> getSimilarCheaperProduct();

    void addNewReview();

    List<User> getTrolls();

    List<InventoryEntity> getOffers();
}
