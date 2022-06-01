package daos;

import entities.CdArtistEntity;
import entities.CdArtistEntityPK;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class CdArtistDao extends GenericDao<CdArtistEntity> implements ICdArtistDao {


    public CdArtistDao(SessionFactory sessionFactory) {
        super(CdArtistEntity.class, sessionFactory);
    }

    @Override
    public CdArtistEntity findOne(CdArtistEntityPK cdArtistEntityPK) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        CdArtistEntity cdArtist = session.get(getDaoClass(), cdArtistEntityPK);
        tx.commit();

        return cdArtist;
    }
}
