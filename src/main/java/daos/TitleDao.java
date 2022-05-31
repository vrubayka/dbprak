package daos;

import entities.ArtistEntity;
import entities.TitleEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

public class TitleDao extends GenericDao<TitleEntity> implements ITitleDao {

    public TitleDao(SessionFactory sessionFactory) {
        super(TitleEntity.class, sessionFactory);
    }

    @Override
    public TitleEntity findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<TitleEntity> query = session.createSelectionQuery(
                "SELECT t FROM TitleEntity t WHERE t.titleName = :titleName", TitleEntity.class);

        query.setParameter("titleName", name);
        TitleEntity title = query.getSingleResultOrNull();
        tx.commit();

        return title;
    }
}
