package daos;

import java.util.List;

public interface IGenericDao <T> {
    T findOne(long id);

    List<T> findAll();

    void create(T entity);

    T update(T entity);

    void delete(T entity);

    void deleteById(long entityId);

    void deleteAll();

}
