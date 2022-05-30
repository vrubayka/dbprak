package daos;

import entities.ArtistEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

public class ArtistDao extends GenericDao<ArtistEntity> implements IArtistDao {

    public ArtistDao(SessionFactory sessionFactory) {
        super(ArtistEntity.class, sessionFactory);
    }

    @Override
    public ArtistEntity findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<ArtistEntity> query = session.createSelectionQuery(
                "SELECT a FROM ArtistEntity a WHERE a.artistName = :artistName", ArtistEntity.class);

        query.setParameter("personName", name);
        ArtistEntity artist = query.getSingleResultOrNull();
        tx.commit();

        return artist;
    }

}
