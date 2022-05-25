package daos;

import entities.PersonEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

public class PersonDao extends GenericDao<PersonEntity> implements IPersonDao {

    public PersonDao(SessionFactory sessionFactory) {
        super(PersonEntity.class, sessionFactory);
    }

    @Override
    public PersonEntity findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery  <PersonEntity> query = session.createSelectionQuery(
                "SELECT p FROM PersonEntity p WHERE p.personName = :personName", PersonEntity.class);

        query.setParameter("personName", name);
        PersonEntity person = query.getSingleResultOrNull();
        tx.commit();

        return person;
    }
}
