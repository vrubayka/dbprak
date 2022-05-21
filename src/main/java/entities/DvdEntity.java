package entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "dvd", schema = "public", catalog = "dbprak")
public class DvdEntity {
    @Id
    @Column(name = "dvd_id", nullable = false)
    private String dvdId;
    @Basic
    @Column(name = "movie_id", nullable = false)
    private int movieId;
    @Basic
    @Column(name = "format", nullable = false, length = 50)
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

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
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
        return dvdId.equals(dvdEntity.dvdId) && movieId == dvdEntity.movieId && termInSec == dvdEntity.termInSec &&
               regionCode == dvdEntity.regionCode && Objects.equals(format, dvdEntity.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dvdId, movieId, format, termInSec, regionCode);
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
