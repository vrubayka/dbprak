package entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "artist", schema = "public", catalog = "dbprak")
public class ArtistEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "artist_id", nullable = false)
    private long artistId;
    @Basic
    @Column(name = "artist_name", nullable = false)
    private String artistName;
    @OneToMany(mappedBy = "artistByArtistId",
               cascade = CascadeType.ALL)
    private Collection<TitleEntity> titlesByArtistId;

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistEntity that = (ArtistEntity) o;
        return artistId == that.artistId && Objects.equals(artistName, that.artistName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistId, artistName);
    }

    public Collection<TitleEntity> getTitlesByArtistId() {
        return titlesByArtistId;
    }

    public void setTitlesByArtistId(Collection<TitleEntity> titlesByArtistId) {
        this.titlesByArtistId = titlesByArtistId;
    }
}
