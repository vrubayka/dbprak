package entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "product_category", schema = "public", catalog = "dbprak")
@IdClass(ProductCategoryEntityPK.class)
public class ProductCategoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "prod_id", nullable = false)
    private long prodId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "category_id", nullable = false)
    private long categoryId;
    @ManyToOne
    @JoinColumn(name = "prod_id", referencedColumnName = "prod_id", nullable = false, insertable = false, updatable = false)
    private ProductEntity productByProdId;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false, insertable = false, updatable = false)
    private CategoryEntity categoryByCategoryId;

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
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
