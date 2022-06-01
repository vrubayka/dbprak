package entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "product_category", schema = "public", catalog = "dbprak")
@IdClass(ProductCategoryEntityPK.class)
public class ProductCategoryEntity {
    @Id
    @Column(name = "prod_id", nullable = false)
    private String prodId;
    
    @Id
    @Column(name = "category_id", nullable = false)
    private long categoryId;
    @ManyToOne
    @JoinColumn(name = "prod_id", referencedColumnName = "prod_id", nullable = false, insertable = false, updatable = false)
    private ProductEntity productByProdId;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false, insertable = false, updatable = false)
    private CategoryEntity categoryByCategoryId;

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategoryEntity that = (ProductCategoryEntity) o;
        return prodId == that.prodId && categoryId == that.categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodId, categoryId);
    }

    public ProductEntity getProductByProdId() {
        return productByProdId;
    }

    public void setProductByProdId(ProductEntity productByProdId) {
        this.productByProdId = productByProdId;
    }

    public CategoryEntity getCategoryByCategoryId() {
        return categoryByCategoryId;
    }

    public void setCategoryByCategoryId(CategoryEntity categoryByCategoryId) {
        this.categoryByCategoryId = categoryByCategoryId;
    }
}
