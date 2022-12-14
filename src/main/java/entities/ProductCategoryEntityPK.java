package entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class ProductCategoryEntityPK implements Serializable {
    @Column(name = "prod_id", nullable = false)
    @Id
    private String prodId;
    @Column(name = "category_id", nullable = false)
    @Id
    private long categoryId;

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
        ProductCategoryEntityPK that = (ProductCategoryEntityPK) o;
        return prodId.equals(that.prodId) && categoryId == that.categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodId, categoryId);
    }
}
