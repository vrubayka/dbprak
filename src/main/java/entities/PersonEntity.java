package entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "person", schema = "public", catalog = "dbprak")
public class PersonEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "person_id", nullable = false)
    private long personId;
    @Column(name = "person_name", unique = true, length = 50)
    private String personName;
    @OneToMany(mappedBy = "personByPersonId")
    private Collection<AuthorEntity> authorsByPersonId;
    @OneToMany(mappedBy = "personByPersonId")
    private Collection<DvdPersonEntity> dvdPeopleByPersonId;

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEntity that = (PersonEntity) o;
        return personId == that.personId && Objects.equals(personName, that.personName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, personName);
    }

    public Collection<AuthorEntity> getAuthorsByPersonId() {
        return authorsByPersonId;
    }

    public void setAuthorsByPersonId(Collection<AuthorEntity> authorsByPersonId) {
        this.authorsByPersonId = authorsByPersonId;
    }

    public Collection<DvdPersonEntity> getDvdPeopleByPersonId() {
        return dvdPeopleByPersonId;
    }

    public void setDvdPeopleByPersonId(Collection<DvdPersonEntity> dvdPeopleByPersonId) {
        this.dvdPeopleByPersonId = dvdPeopleByPersonId;
    }
}
