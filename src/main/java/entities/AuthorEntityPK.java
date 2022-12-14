package entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class AuthorEntityPK implements Serializable {
    @Column(name = "book_id", nullable = false)
    @Id
    private String bookId;
    @Column(name = "person_id", nullable = false)
    @Id
    private long personId;

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
        AuthorEntityPK that = (AuthorEntityPK) o;
        return bookId.equals(that.bookId) && personId == that.personId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, personId);
    }
}
