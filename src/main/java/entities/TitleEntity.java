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
    @Column(name = "title_name", nullable = false, length = 255)
    private String titleName;
    @OneToMany(mappedBy = "titleByTitleId")
    private Collection<CdTitleEntity> cdTitlesByTitleId;

    public long getTitleId() {
        return titleId;
    }

    public void setTitleId(long titleId) {
        this.titleId = titleId;
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
        return titleId == that.titleId && Objects.equals(titleName, that.titleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titleId, titleName);
    }

    public Collection<CdTitleEntity> getCdTitlesByTitleId() {
        return cdTitlesByTitleId;
    }

    public void setCdTitlesByTitleId(Collection<CdTitleEntity> cdTitlesByTitleId) {
        this.cdTitlesByTitleId = cdTitlesByTitleId;
    }

}
