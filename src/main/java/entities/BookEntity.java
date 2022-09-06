package entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "book", schema = "public", catalog = "dbprak")
public class BookEntity {
    @Override
    public String toString() {
        return "ISBN: " + isbn + "\nPublisher: " + publisher + "\nRelease Date: " + releaseDate +
                "\nPages: " + pages + "\nAuthors: " + authorsByBookId;
    }

    @Id
    @Column(name = "book_id", nullable = false)
    private String bookId;
    @Basic
    @Column(name = "isbn", length = 50)
    private String isbn;
    @Basic
    @Column(name = "publisher", nullable = false)
    private String publisher;
    @Basic
    @Column(name = "release_date")
    private Date releaseDate;
    @Basic
    @Column(name = "pages", nullable = false)
    private int pages;
    @OneToMany(mappedBy = "bookByBookId",
               cascade = CascadeType.ALL)
    private Collection<AuthorEntity> authorsByBookId;
    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "prod_id", nullable = false)
    private ProductEntity productByBookId;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return bookId.equals(that.bookId) && pages == that.pages && Objects.equals(isbn, that.isbn) &&
               Objects.equals(publisher, that.publisher) &&
               Objects.equals(releaseDate, that.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, isbn, publisher, releaseDate, pages);
    }

    public Collection<AuthorEntity> getAuthorsByBookId() {
        return authorsByBookId;
    }

    public void setAuthorsByBookId(Collection<AuthorEntity> authorsByBookId) {
        this.authorsByBookId = authorsByBookId;
    }

    public ProductEntity getProductByBookId() {
        return productByBookId;
    }

    public void setProductByBookId(ProductEntity productByBookId) {
        this.productByBookId = productByBookId;
    }
}
