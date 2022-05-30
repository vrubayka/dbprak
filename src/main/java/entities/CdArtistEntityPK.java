package entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class CdArtistEntityPK implements Serializable {
    @Column(name = "cd_id", nullable = false, length = 255)
    @Id
    private String cdId;
    @Column(name = "artist_id", nullable = false)
    @Id
    private long artistId;

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
        CdArtistEntityPK that = (CdArtistEntityPK) o;
        return artistId == that.artistId && Objects.equals(cdId, that.cdId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cdId, artistId);
    }
}
