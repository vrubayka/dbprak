package entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "cd", schema = "public", catalog = "dbprak")
public class CdEntity {
    @Override
    public String toString() {
        String output = "Label: " + label + "\nRelease Date: " + releaseDate;
        return output;
    }

    @Id
    @Column(name = "cd_id", nullable = false, length = 255)
    private String cdId;
    @Basic
    @Column(name = "label", nullable = false, length = 255)
    private String label;
    @Basic
    @Column(name = "release_date")
    private Date releaseDate;
    @OneToOne
    @JoinColumn(name = "cd_id", referencedColumnName = "prod_id", nullable = false)
    private ProductEntity productByCdId;
    @OneToMany(mappedBy = "cdByCdId",
               cascade = CascadeType.ALL)
    private Collection<CdTitleEntity> cdTitlesByCdId;
    @OneToMany(mappedBy = "cdByCdId")
    private Collection<CdArtistEntity> cdArtistsByCdId;

    public String getCdId() {
        return cdId;
    }

    public void setCdId(String cdId) {
        this.cdId = cdId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CdEntity cdEntity = (CdEntity) o;
        return cdId.equals(cdEntity.cdId) && Objects.equals(label, cdEntity.label) &&
               Objects.equals(releaseDate, cdEntity.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cdId, label, releaseDate);
    }

    public ProductEntity getProductByCdId() {
        return productByCdId;
    }

    public void setProductByCdId(ProductEntity productByCdId) {
        this.productByCdId = productByCdId;
    }

    public Collection<CdTitleEntity> getCdTitlesByCdId() {
        return cdTitlesByCdId;
    }

    public void setCdTitlesByCdId(Collection<CdTitleEntity> cdTitlesByCdId) {
        this.cdTitlesByCdId = cdTitlesByCdId;
    }

    public Collection<CdArtistEntity> getCdArtistsByCdId() {
        return cdArtistsByCdId;
    }

    public void setCdArtistsByCdId(Collection<CdArtistEntity> cdArtistsByCdId) {
        this.cdArtistsByCdId = cdArtistsByCdId;
    }
}
