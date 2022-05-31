package entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "cd_title", schema = "public", catalog = "dbprak")
@IdClass(CdTitleEntityPK.class)
public class CdTitleEntity {
    @Id
    @Column(name = "cd_id", nullable = false)
    private String cdId;

    @Id
    @Column(name = "title_id", nullable = false)
    private long titleId;
    @ManyToOne
    @JoinColumn(name = "cd_id", referencedColumnName = "cd_id", nullable = false, insertable = false, updatable = false)
    private CdEntity cdByCdId;
    @ManyToOne
    @JoinColumn(name = "title_id", referencedColumnName = "title_id", nullable = false, insertable = false, updatable = false)
    private TitleEntity titleByTitleId;

    public String getCdId() {
        return cdId;
    }

    public void setCdId(String cdId) {
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
        CdTitleEntity that = (CdTitleEntity) o;
        return cdId.equals(that.cdId) && titleId == that.titleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cdId, titleId);
    }

    public CdEntity getCdByCdId() {
        return cdByCdId;
    }

    public void setCdByCdId(CdEntity cdByCdId) {
        this.cdByCdId = cdByCdId;
    }

    public TitleEntity getTitleByTitleId() {
        return titleByTitleId;
    }

    public void setTitleByTitleId(TitleEntity titleByTitleId) {
        this.titleByTitleId = titleByTitleId;
    }
}
