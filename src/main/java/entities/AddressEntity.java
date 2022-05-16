package entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "address", schema = "public", catalog = "dbprak")
public class AddressEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "address_id", nullable = false)
    private long addressId;
    @Basic
    @Column(name = "street_name", nullable = false, length = 50)
    private String streetName;
    @Basic
    @Column(name = "street_number", nullable = false)
    private int streetNumber;
    @Basic
    @Column(name = "city", nullable = false, length = 50)
    private String city;
    @Basic
    @Column(name = "postcode", nullable = false, length = 50)
    private String postcode;
    @Basic
    @Column(name = "country", length = 50)
    private String country;
    @OneToMany(mappedBy = "addressByAddressId")
    private Collection<CustomerEntity> customersByAddressId;
    @OneToMany(mappedBy = "addressByAddressId")
    private Collection<StoreEntity> storesByAddressId;

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressEntity that = (AddressEntity) o;
        return addressId == that.addressId && streetNumber == that.streetNumber &&
               Objects.equals(streetName, that.streetName) && Objects.equals(city, that.city) &&
               Objects.equals(postcode, that.postcode) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressId, streetName, streetNumber, city, postcode, country);
    }

    public Collection<CustomerEntity> getCustomersByAddressId() {
        return customersByAddressId;
    }

    public void setCustomersByAddressId(Collection<CustomerEntity> customersByAddressId) {
        this.customersByAddressId = customersByAddressId;
    }

    public Collection<StoreEntity> getStoresByAddressId() {
        return storesByAddressId;
    }

    public void setStoresByAddressId(Collection<StoreEntity> storesByAddressId) {
        this.storesByAddressId = storesByAddressId;
    }
}
