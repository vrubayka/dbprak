package entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class ReviewEntityPK implements Serializable {
    @Column(name = "prod_id", nullable = false)
    @Id
    private String prodId;
    @Column(name = "username", nullable = false, length = 50)
    @Id
    private String username;

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewEntityPK that = (ReviewEntityPK) o;
        return prodId.equals(that.prodId) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodId, username);
    }
}
