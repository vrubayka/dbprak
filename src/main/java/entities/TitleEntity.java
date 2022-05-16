package entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "title", schema = "public", catalog = "dbprak")
public class TitleEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "title_id", nullable = false)
    private long titleId;
    @Basic
    @Column(name = "artist_id", nullable = false)
    private long artistId;
    @Basic
    @Column(name = "title_name", nullable = false, length = 255)
    private String titleName;
    @OneToMany(mappedBy = "titleByTitleId")
    private Collection<CdTitleEntity> cdTitlesByTitleId;
    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "artist_id", nullable = false, insertable = false, updatable = false)
    private ArtistEntity artistByArtistId;

    public long getTitleId() {
        return titleId;
    }

    public void setTitleId(long titleId) {
        this.titleId = titleId;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TitleEntity that = (TitleEntity) o;
        return titleId == that.titleId && artistId == that.artistId && Objects.equals(titleName, that.titleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titleId, artistId, titleName);
    }

    public Collection<CdTitleEntity> getCdTitlesByTitleId() {
        return cdTitlesByTitleId;
    }

    public void setCdTitlesByTitleId(Collection<CdTitleEntity> cdTitlesByTitleId) {
        this.cdTitlesByTitleId = cdTitlesByTitleId;
    }

    public ArtistEntity getArtistByArtistId() {
        return artistByArtistId;
    }

    public void setArtistByArtistId(ArtistEntity artistByArtistId) {
        this.artistByArtistId = artistByArtistId;
    }
}
