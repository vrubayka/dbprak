package entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "inventory", schema = "public", catalog = "dbprak")
@IdClass(InventoryEntityPK.class)
public class InventoryEntity {

    @Id
    @Column(name = "store_id", nullable = false)
    private long storeId;
    @Id
    @Column(name = "prod_id", nullable = false)
    private String prodId;
    @Basic
    @Column(name = "price")
    private BigDecimal price;
    @Basic
    @Column(name = "condition", length = 50)
    private String condition;
    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "store_id", insertable = false, updatable = false)
    private StoreEntity storeByStoreId;
    @ManyToOne
    @JoinColumn(name = "prod_id", referencedColumnName = "prod_id", insertable = false, updatable = false)
    private ProductEntity productByProdId;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryEntity that = (InventoryEntity) o;
        return (storeId == that.storeId) && (prodId.equals(that.prodId)) &&
               (price.compareTo(that.price) == 0) && condition.equals(that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, prodId, price, condition);
    }

    public StoreEntity getStoreByStoreId() {
        return storeByStoreId;
    }

    public void setStoreByStoreId(StoreEntity storeByStoreId) {
        this.storeByStoreId = storeByStoreId;
    }

    public ProductEntity getProductByProdId() {
        return productByProdId;
    }

    public void setProductByProdId(ProductEntity productByProdId) {
        this.productByProdId = productByProdId;
    }
}
