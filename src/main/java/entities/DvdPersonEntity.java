package entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "dvd_person", schema = "public", catalog = "dbprak")
@IdClass(DvdPersonEntityPK.class)
public class DvdPersonEntity {
    @Id
    @Column(name = "dvd_id", nullable = false)
    private String dvdId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "person_id", nullable = false)
    private long personId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "p_role", nullable = false, length = 50)
    private String pRole;
    @ManyToOne
    @JoinColumn(name = "dvd_id", referencedColumnName = "dvd_id", nullable = false, insertable = false, updatable = false)
    private DvdEntity dvdByDvdId;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = false, insertable = false, updatable = false)
    private PersonEntity personByPersonId;

    public String getDvdId() {
        return dvdId;
    }

    public void setDvdId(String dvdId) {
        this.dvdId = dvdId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getpRole() {
        return pRole;
    }

    public void setpRole(String pRole) {
        this.pRole = pRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DvdPersonEntity that = (DvdPersonEntity) o;
        return dvdId.equals(that.dvdId) && personId == that.personId && Objects.equals(pRole, that.pRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dvdId, personId, pRole);
    }

    public DvdEntity getDvdByDvdId() {
        return dvdByDvdId;
    }

    public void setDvdByDvdId(DvdEntity dvdByDvdId) {
        this.dvdByDvdId = dvdByDvdId;
    }

    public PersonEntity getPersonByPersonId() {
        return personByPersonId;
    }

    public void setPersonByPersonId(PersonEntity personByPersonId) {
        this.personByPersonId = personByPersonId;
    }
}
