package entities;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "product", schema = "public", catalog = "dbprak")
public class ProductEntity {
    @Id
    @Column(name = "prod_id", nullable = false)
    private String prodId;
    @Basic
    @Column(name = "prod_name", nullable = false, length = 255)
    private String prodName;
    @Basic
    @Column(name = "rating", nullable = false, precision = 0)
    private double rating;
    @Basic
    @Column(name = "sales_rank", nullable = false)
    private int salesRank;
    @Basic
    @Column(name = "image", nullable = true)
    private String image;
    @OneToOne(mappedBy = "productByBookId",
              cascade = CascadeType.ALL)
    private BookEntity bookByProdId;
    @OneToOne(mappedBy = "productByCdId",
              cascade = CascadeType.ALL)
    private CdEntity cdByProdId;
    @OneToOne(mappedBy = "productByDvdId",
              cascade = CascadeType.ALL)
    private DvdEntity dvdByProdId;
    @OneToMany(mappedBy = "productByProdId",
               cascade = CascadeType.ALL)
    private Collection<InventoryEntity> inventoriesByProdId;
    @OneToMany(mappedBy = "productByProdId",
               cascade = CascadeType.ALL)
    private Collection<ProductCategoryEntity> productCategoriesByProdId;
    @OneToMany(mappedBy = "productByProdId",
               cascade = CascadeType.ALL)
    private Collection<ReviewEntity> reviewsByProdId;

    public ProductEntity() {
    }

    public ProductEntity(String prodName, double rating, int salesRank) {
        this.prodName  = prodName;
        this.rating    = rating;
        this.salesRank = salesRank;
        this.image     = image;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getSalesRank() {
        return salesRank;
    }

    public void setSalesRank(int salesRank) {
        this.salesRank = salesRank;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return prodId == that.prodId && Double.compare(that.rating, rating) == 0 && salesRank == that.salesRank &&
               Objects.equals(prodName, that.prodName) && image.equals(that.image);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(prodId, prodName, rating, salesRank, image);
        return result;
    }

    public BookEntity getBookByProdId() {
        return bookByProdId;
    }

    public void setBookByProdId(BookEntity bookByProdId) {
        this.bookByProdId = bookByProdId;
    }

    public CdEntity getCdByProdId() {
        return cdByProdId;
    }

    public void setCdByProdId(CdEntity cdByProdId) {
        this.cdByProdId = cdByProdId;
    }

    public DvdEntity getDvdByProdId() {
        return dvdByProdId;
    }

    public void setDvdByProdId(DvdEntity dvdByProdId) {
        this.dvdByProdId = dvdByProdId;
    }

    public Collection<InventoryEntity> getInventoriesByProdId() {
        return inventoriesByProdId;
    }

    public void setInventoriesByProdId(Collection<InventoryEntity> inventoriesByProdId) {
        this.inventoriesByProdId = inventoriesByProdId;
    }

    public Collection<ProductCategoryEntity> getProductCategoriesByProdId() {
        return productCategoriesByProdId;
    }

    public void setProductCategoriesByProdId(Collection<ProductCategoryEntity> productCategoriesByProdId) {
        this.productCategoriesByProdId = productCategoriesByProdId;
    }

    public Collection<ReviewEntity> getReviewsByProdId() {
        return reviewsByProdId;
    }

    public void setReviewsByProdId(Collection<ReviewEntity> reviewsByProdId) {
        this.reviewsByProdId = reviewsByProdId;
    }
}
