package entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "cd_artist", schema = "public", catalog = "dbprak")
@IdClass(CdArtistEntityPK.class)
public class CdArtistEntity {

    @Id
    @Column(name = "cd_id", nullable = false, length = 255)
    private String cdId;

    @Id
    @Column(name = "artist_id", nullable = false)
    private long artistId;
    @ManyToOne
    @JoinColumn(name = "cd_id", referencedColumnName = "cd_id", nullable = false, insertable = false, updatable = false)
    private CdEntity cdByCdId;
    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "artist_id", nullable = false, insertable = false,
                updatable = false)
    private ArtistEntity artistByArtistId;

    public String getCdId() {
        return cdId;
    }

    public void setCdId(String cdId) {
        this.cdId = cdId;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CdArtistEntity that = (CdArtistEntity) o;
        return artistId == that.artistId && Objects.equals(cdId, that.cdId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cdId, artistId);
    }

    public CdEntity getCdByCdId() {
        return cdByCdId;
    }

    public void setCdByCdId(CdEntity cdByCdId) {
        this.cdByCdId = cdByCdId;
    }

    public ArtistEntity getArtistByArtistId() {
        return artistByArtistId;
    }

    public void setArtistByArtistId(ArtistEntity artistByArtistId) {
        this.artistByArtistId = artistByArtistId;
    }
}
