package daos;

import entities.AuthorEntity;
import entities.DvdPersonEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.List;

public class DvdPersonDao extends GenericDao<DvdPersonEntity> {

    public DvdPersonDao(SessionFactory sessionFactory) {
        super(DvdPersonEntity.class, sessionFactory);
    }

    public List<DvdPersonEntity> findPersonByDvdId (String dvdId){
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<DvdPersonEntity> query = session.createSelectionQuery(
                        "FROM DvdPersonEntity dvdp WHERE dvdp.dvdId = :dvdId", DvdPersonEntity.class)
                .setParameter("dvdId", dvdId);
        List<DvdPersonEntity> people = query.getResultList();
        tx.commit();
        return people;
    }
}
