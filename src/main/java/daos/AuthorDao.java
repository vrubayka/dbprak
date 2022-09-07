package daos;

import entities.ArtistEntity;
import entities.AuthorEntity;
import entities.InventoryEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.List;

public class AuthorDao extends GenericDao<AuthorEntity> {

    public AuthorDao(SessionFactory sessionFactory) {
        super(AuthorEntity.class, sessionFactory);
    }

    public List<AuthorEntity> findAuthorByBookId (String bookId){
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<AuthorEntity> query = session.createSelectionQuery(
                        "FROM AuthorEntity ie WHERE ie.bookId = :bookId", AuthorEntity.class)
                .setParameter("bookId", bookId);
        List<AuthorEntity> authors = query.getResultList();
        tx.commit();
        return authors;
    }
}
