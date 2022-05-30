package entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class DvdPersonEntityPK implements Serializable {
    @Column(name = "dvd_id", nullable = false)
    @Id
    private String dvdId;
    @Column(name = "person_id", nullable = false)
    @Id
    private long personId;
    @Column(name = "p_role", nullable = false, length = 50)
    @Id
    private String pRole;

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
        DvdPersonEntityPK that = (DvdPersonEntityPK) o;
        return dvdId.equals(that.dvdId) && personId == that.personId && Objects.equals(pRole, that.pRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dvdId, personId, pRole);
    }
}
