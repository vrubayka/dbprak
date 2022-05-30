package entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "author", schema = "public", catalog = "dbprak")
@IdClass(AuthorEntityPK.class)
public class AuthorEntity {
    @Id
    @Column(name = "book_id", nullable = false)
    private String bookId;
    @Id
    @Column(name = "person_id", nullable = false)
    private long personId;
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", nullable = false, insertable = false, updatable = false)
    private BookEntity bookByBookId;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = false, insertable = false, updatable = false)
    private PersonEntity personByPersonId;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorEntity that = (AuthorEntity) o;
        return bookId.equals(that.bookId) && personId == that.personId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, personId);
    }

    public BookEntity getBookByBookId() {
        return bookByBookId;
    }

    public void setBookByBookId(BookEntity bookByBookId) {
        this.bookByBookId = bookByBookId;
    }

    public PersonEntity getPersonByPersonId() {
        return personByPersonId;
    }

    public void setPersonByPersonId(PersonEntity personByPersonId) {
        this.personByPersonId = personByPersonId;
    }
}
