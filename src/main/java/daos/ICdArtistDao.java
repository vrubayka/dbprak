package daos;

import entities.CdArtistEntity;
import entities.CdArtistEntityPK;

public interface ICdArtistDao extends IGenericDao<CdArtistEntity> {


    CdArtistEntity findOne(CdArtistEntityPK cdArtistEntityPK);
}
