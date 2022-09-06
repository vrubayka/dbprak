package middle;

import entities.InventoryEntity;
import entities.ProductEntity;
import middle.wrapperClass.CategoryNode;
import middle.wrapperClass.User;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

public interface IMenuMapper {

    void init(boolean reload);

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
