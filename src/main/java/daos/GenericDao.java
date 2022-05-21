package daos;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class GenericDao <T> implements IGenericDao <T>{

    private Class<T> daoClass;
    SessionFactory sessionFactory;

    public GenericDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public GenericDao(Class<T> daoClass, SessionFactory sessionFactory) {
        this.daoClass       = daoClass;
        this.sessionFactory = sessionFactory;
    }


    @Override
    public T findOne(long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(daoClass, id);
    }

    @Override
    public List<T> findAll() {
        Session session = sessionFactory.getCurrentSession();
        String s = daoClass.getName();
        s = s.replaceAll("Entity$", "");
        s = "from " + s;
        return session.createQuery(s, daoClass).getResultList();
    }

    @Override
    public void create(T entity) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.persist(entity);
        tx.commit();
    }

    @Override
    public T update(T entity) {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        T updatedEntity = session.merge(entity);
        tx.commit();

        return updatedEntity;
    }

    @Override
    public void delete(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(entity);
    }

    @Override
    public void deleteById(long entityId) {
        Session session = sessionFactory.getCurrentSession();
        T entity = findOne(entityId);
        session.remove(entity);
    }


}
