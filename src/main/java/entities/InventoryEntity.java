package entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "inventory", schema = "public", catalog = "dbprak")
public class InventoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "store_id")
    private Long storeId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "prod_id")
    private Long prodId;
    @Basic
    @Column(name = "prize", nullable = false)
    private BigDecimal prize;
    @Basic
    @Column(name = "condition", length = 50)
    private String condition;
    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "store_id")
    private StoreEntity storeByStoreId;
    @ManyToOne
    @JoinColumn(name = "prod_id", referencedColumnName = "prod_id")
    private ProductEntity productByProdId;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public BigDecimal getPrize() {
        return prize;
    }

    public void setPrize(BigDecimal prize) {
        this.prize = prize;
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
        return Objects.equals(storeId, that.storeId) && Objects.equals(prodId, that.prodId) &&
               Objects.equals(prize, that.prize) && Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, prodId, prize, condition);
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
