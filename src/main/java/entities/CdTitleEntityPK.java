package entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class CdTitleEntityPK implements Serializable {
    @Column(name = "cd_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cdId;
    @Column(name = "title_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long titleId;

    public long getCdId() {
        return cdId;
    }

    public void setCdId(long cdId) {
        this.cdId = cdId;
    }

    public long getTitleId() {
        return titleId;
    }

    public void setTitleId(long titleId) {
        this.titleId = titleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CdTitleEntityPK that = (CdTitleEntityPK) o;
        return cdId == that.cdId && titleId == that.titleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cdId, titleId);
    }
}
