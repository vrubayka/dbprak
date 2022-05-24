package entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "review", schema = "public", catalog = "dbprak")
@IdClass(ReviewEntityPK.class)
public class ReviewEntity {

    @Id
    @Column(name = "prod_id", nullable = false)
    private String prodId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "username", nullable = false, length = 50)
    private String username;
    @Basic
    @Column(name = "reviewdate", nullable = false)
    private Date reviewdate;
    @Basic
    @Column(name = "rating", nullable = false)
    private int rating;
    @Basic
    @Column(name = "helpful_rating", nullable = false)
    private int helpfulRating;
    @Basic
    @Column(name = "review_sum")
    private String reviewSum;
    @Basic
    @Column(name = "review_text", length = 5000)
    private String reviewText;
    @ManyToOne
    @JoinColumn(name = "prod_id", referencedColumnName = "prod_id", nullable = false, insertable = false, updatable = false)
    private ProductEntity productByProdId;

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

    public Date getReviewdate() {
        return reviewdate;
    }

    public void setReviewdate(Date reviewdate) {
        this.reviewdate = reviewdate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getHelpfulRating() {
        return helpfulRating;
    }

    public void setHelpfulRating(int helpfulRating) {
        this.helpfulRating = helpfulRating;
    }

    public String getReviewSum() {
        return reviewSum;
    }

    public void setReviewSum(String reviewSum) {
        this.reviewSum = reviewSum;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewEntity that = (ReviewEntity) o;
        return prodId == that.prodId && rating == that.rating && helpfulRating == that.helpfulRating &&
               Objects.equals(username, that.username) && Objects.equals(reviewdate, that.reviewdate) &&
               Objects.equals(reviewSum, that.reviewSum) && Objects.equals(reviewText, that.reviewText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodId, username, reviewdate, rating, helpfulRating, reviewSum, reviewText);
    }

    public ProductEntity getProductByProdId() {
        return productByProdId;
    }

    public void setProductByProdId(ProductEntity productByProdId) {
        this.productByProdId = productByProdId;
    }
}
