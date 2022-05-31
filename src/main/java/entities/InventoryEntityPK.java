package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class InventoryEntityPK implements Serializable {
    @Column(name = "store_id", nullable = false)
    @Id
    private long storeId;
    @Column(name = "prod_id", nullable = false)
    @Id
    private String prodId;

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryEntityPK that = (InventoryEntityPK) o;
        return storeId == that.storeId && Objects.equals(prodId, that.prodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, prodId);
    }
//    public Long getStoreId() {
//        return storeId;
//    }
//
//    public void setStoreId(long storeId) {
//        this.storeId = storeId;
//    }
//
//    public String getProdId() {
//        return prodId;
//    }
//
//    public void setProdId(String prodId) {
//        this.prodId = prodId;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        InventoryEntityPK that = (InventoryEntityPK) o;
//        return storeId == that.storeId && prodId.equals(that.prodId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(storeId, prodId);
//    }
}
