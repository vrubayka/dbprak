package daos;

import entities.ArtistEntity;

public interface IArtistDao extends IGenericDao<ArtistEntity> {

    ArtistEntity findByName(String name);
}
