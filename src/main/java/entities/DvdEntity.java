package entities;

import daos.DvdPersonDao;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "dvd", schema = "public", catalog = "dbprak")
public class DvdEntity {

    @Override
    public String toString() {
        String output = "Format: " + format + "\nLength: " + termInSec / 60 + "\nRegion Code: " + regionCode + "\nCast:";
        for (DvdPersonEntity dvdPersonEntity : getDvdPeopleByDvdId()) {
            if (dvdPersonEntity.getpRole().equals("Actor")) {
                output = output + "\n" + dvdPersonEntity.getPersonByPersonId().getPersonName();
            }
        }
        for (DvdPersonEntity dvdPersonEntity : getDvdPeopleByDvdId()) {
            if (dvdPersonEntity.getpRole().equals("Director")) {
                output = output + "\nDirected by: " + dvdPersonEntity.getPersonByPersonId().getPersonName();
            } else if (dvdPersonEntity.getpRole().equals("Creator")) {
                output = output + "\nCreated by: " + dvdPersonEntity.getPersonByPersonId().getPersonName();
            }
        }
        return output;
    }

    @Id
    @Column(name = "dvd_id", nullable = false)
    private String dvdId;
    @Basic
    @Column(name = "format", nullable = false)
    private String format;
    @Basic
    @Column(name = "term_in_sec", nullable = false)
    private int termInSec;
    @Basic
    @Column(name = "region_code", nullable = false)
    private int regionCode;
    @OneToOne
    @JoinColumn(name = "dvd_id", referencedColumnName = "prod_id", nullable = false)
    private ProductEntity productByDvdId;
    @OneToMany(mappedBy = "dvdByDvdId",
               cascade = CascadeType.ALL)
    private Collection<DvdPersonEntity> dvdPeopleByDvdId;

    public String getDvdId() {
        return dvdId;
    }

    public void setDvdId(String dvdId) {
        this.dvdId = dvdId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getTermInSec() {
        return termInSec;
    }

    public void setTermInSec(int termInSec) {
        this.termInSec = termInSec;
    }

    public int getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(int regionCode) {
        this.regionCode = regionCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DvdEntity dvdEntity = (DvdEntity) o;
        return dvdId.equals(dvdEntity.dvdId) && termInSec == dvdEntity.termInSec &&
               regionCode == dvdEntity.regionCode && Objects.equals(format, dvdEntity.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dvdId, format, termInSec, regionCode);
    }

    public ProductEntity getProductByDvdId() {
        return productByDvdId;
    }

    public void setProductByDvdId(ProductEntity productByDvdId) {
        this.productByDvdId = productByDvdId;
    }

    public Collection<DvdPersonEntity> getDvdPeopleByDvdId() {
        return dvdPeopleByDvdId;
    }

    public void setDvdPeopleByDvdId(Collection<DvdPersonEntity> dvdPeopleByDvdId) {
        this.dvdPeopleByDvdId = dvdPeopleByDvdId;
    }


}
