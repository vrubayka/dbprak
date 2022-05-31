package entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "similar_products", schema = "public", catalog = "dbprak")
@IdClass(SimilarProductsEntityPK.class)
public class SimilarProductsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "prod_id", nullable = false, length = 255)
    private String prodId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "similar_prod_id", nullable = false, length = 255)
    private String similarProdId;
    @ManyToOne
    @JoinColumn(name = "prod_id", referencedColumnName = "prod_id", nullable = false, insertable = false, updatable = false)
    private ProductEntity productByProdId;
    @ManyToOne
    @JoinColumn(name = "similar_prod_id", referencedColumnName = "prod_id", nullable = false, insertable = false, updatable = false)
    private ProductEntity productBySimilarProdId;

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
        SimilarProductsEntity that = (SimilarProductsEntity) o;
        return Objects.equals(prodId, that.prodId) && Objects.equals(similarProdId, that.similarProdId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodId, similarProdId);
    }

    public ProductEntity getProductByProdId() {
        return productByProdId;
    }

    public void setProductByProdId(ProductEntity productByProdId) {
        this.productByProdId = productByProdId;
    }

    public ProductEntity getProductBySimilarProdId() {
        return productBySimilarProdId;
    }

    public void setProductBySimilarProdId(ProductEntity productBySimilarProdId) {
        this.productBySimilarProdId = productBySimilarProdId;
    }
}
