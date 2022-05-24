package daos;

import jakarta.persistence.Table;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;

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
        session.persist(entity);
    }

    @Override
    public void update(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(entity);
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

    @Override
    public void deleteAll() {
        Session session = sessionFactory.getCurrentSession();
        Table table = daoClass.getAnnotation(Table.class);
        String tableName = table.name();
        MutationQuery query = session.createMutationQuery("DELETE FROM :tableName");
        query.setParameter("tableName", tableName);
        query.executeUpdate();
    }


}
