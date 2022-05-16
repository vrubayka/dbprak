package entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "store", schema = "public", catalog = "dbprak")
public class StoreEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "store_id", nullable = false)
    private long storeId;
    @Basic
    @Column(name = "address_id", nullable = false)
    private long addressId;
    @Basic
    @Column(name = "store_name", nullable = false, length = 255)
    private String storeName;
    @OneToMany(mappedBy = "storeByStoreId",
               cascade = CascadeType.ALL)
    private Collection<InventoryEntity> inventoriesByStoreId;
    @OneToMany(mappedBy = "storeByStoreId")
    private Collection<OrderEntity> ordersByStoreId;
    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "address_id", nullable = false, insertable = false, updatable = false)
    private AddressEntity addressByAddressId;

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreEntity that = (StoreEntity) o;
        return storeId == that.storeId && addressId == that.addressId &&
               Objects.equals(storeName, that.storeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, addressId, storeName);
    }

    public Collection<InventoryEntity> getInventoriesByStoreId() {
        return inventoriesByStoreId;
    }

    public void setInventoriesByStoreId(Collection<InventoryEntity> inventoriesByStoreId) {
        this.inventoriesByStoreId = inventoriesByStoreId;
    }

    public Collection<OrderEntity> getOrdersByStoreId() {
        return ordersByStoreId;
    }

    public void setOrdersByStoreId(Collection<OrderEntity> ordersByStoreId) {
        this.ordersByStoreId = ordersByStoreId;
    }

    public AddressEntity getAddressByAddressId() {
        return addressByAddressId;
    }

    public void setAddressByAddressId(AddressEntity addressByAddressId) {
        this.addressByAddressId = addressByAddressId;
    }
}
