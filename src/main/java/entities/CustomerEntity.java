package entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "customer", schema = "public", catalog = "dbprak")
public class CustomerEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "customer_id", nullable = false)
    private long customerId;
    @Basic
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Basic
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Basic
    @Column(name = "address_id", nullable = false)
    private long addressId;
    @Basic
    @Column(name = "bankaccount", nullable = false, length = 50)
    private String bankaccount;
    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "address_id", nullable = false, insertable = false, updatable = false)
    private AddressEntity addressByAddressId;
    @OneToMany(mappedBy = "customerByCustomerId")
    private Collection<OrderEntity> ordersByCustomerId;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity that = (CustomerEntity) o;
        return customerId == that.customerId && addressId == that.addressId &&
               Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) &&
               Objects.equals(bankaccount, that.bankaccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, firstName, lastName, addressId, bankaccount);
    }

    public AddressEntity getAddressByAddressId() {
        return addressByAddressId;
    }

    public void setAddressByAddressId(AddressEntity addressByAddressId) {
        this.addressByAddressId = addressByAddressId;
    }

    public Collection<OrderEntity> getOrdersByCustomerId() {
        return ordersByCustomerId;
    }

    public void setOrdersByCustomerId(Collection<OrderEntity> ordersByCustomerId) {
        this.ordersByCustomerId = ordersByCustomerId;
    }
}
