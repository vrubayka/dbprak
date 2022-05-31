package entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class SimilarProductsEntityPK implements Serializable {
    @Column(name = "prod_id", nullable = false, length = 255)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String prodId;
    @Column(name = "similar_prod_id", nullable = false, length = 255)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String similarProdId;

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getSimilarProdId() {
        return similarProdId;
    }

    public void setSimilarProdId(String similarProdId) {
        this.similarProdId = similarProdId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimilarProductsEntityPK that = (SimilarProductsEntityPK) o;
        return Objects.equals(prodId, that.prodId) && Objects.equals(similarProdId, that.similarProdId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodId, similarProdId);
    }
}
