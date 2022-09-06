package daos;

import entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.List;

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

    public List<CdArtistEntity> findArtistForCd (String cdId) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<CdArtistEntity> query = session.createSelectionQuery(
                        "FROM CdArtistEntity cda WHERE cda.cdId = :cdId", CdArtistEntity.class)
                .setParameter("cdId", cdId);
        return query.getResultList();
    }

}
