package middle;

import entities.InventoryEntity;
import entities.ProductEntity;
import entities.ReviewEntity;
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

    ReviewEntity addNewReview(ReviewEntity review, boolean newReview);

    List<User> getTrolls(Double rating);

    List<InventoryEntity> getOffers(String prodId);
}
