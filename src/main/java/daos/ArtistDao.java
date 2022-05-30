package daos;

import entities.ArtistEntity;
import org.hibernate.SessionFactory;

public class ArtistDao extends GenericDao<ArtistEntity> implements IArtistDao {

    public ArtistDao(SessionFactory sessionFactory) {
        super(ArtistEntity.class, sessionFactory);
    }

    @Override
    public ArtistEntity findByName(String name) {
        return null;
    }

}
