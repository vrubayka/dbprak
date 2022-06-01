package parser;

import com.opencsv.bean.CsvBindByName;

import java.util.Date;

public class CSVBean {
    @CsvBindByName(column = "product", required = true)
    private String prodId;

    @CsvBindByName(column = "rating", required = true)
    private Integer rating;

    @CsvBindByName(column = "helpful", required = true)
    private Integer helpful_rating;

    @CsvBindByName(column = "reviewdate", required = true)
    private java.sql.Date reviewdate;

    @CsvBindByName(column = "user", required = true)
    private String username;

    @CsvBindByName(column = "summary", required = true)
    private String reviewSum;

    @CsvBindByName(column = "content", required = true)
    private String review_text;


    //setters, getters

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getHelpful_rating() {
        return helpful_rating;
    }

    public void setHelpful_rating(Integer helpful_rating) {
        this.helpful_rating = helpful_rating;
    }

    public java.sql.Date getReviewdate() {
        return reviewdate;
    }

    public void setReviewdate(java.sql.Date reviewdate) {
        this.reviewdate = reviewdate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReviewSum() {
        return reviewSum;
    }

    public void setReviewSum(String reviewSum) {
        this.reviewSum = reviewSum;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

}
