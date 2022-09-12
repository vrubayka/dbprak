package middle;

import entities.InventoryEntity;
import entities.ProductEntity;
import middle.wrapperClass.CategoryNode;
import middle.wrapperClass.User;

import java.util.List;

public interface IMenuMapper {

    void init(boolean reload);

    void finish();

    ProductEntity getProduct(String id);

    List<ProductEntity> getProducts(String pattern);

    CategoryNode getCategoryTree();

    List<ProductEntity> getProductsByCategoryPath(String path);

    List<Object[]> getTopProducts(int k);

    List<ProductEntity> getSimilarCheaperProduct(String id);

    void addNewReview();

    List<User> getTrolls(Double rating);

    List<InventoryEntity> getOffers();
}
