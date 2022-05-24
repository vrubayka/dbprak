package queries;

import daos.GenericDao;
import entities.*;
import org.hibernate.SessionFactory;

public class HibernateQueries {

    SessionFactory sessionFactory;

    public HibernateQueries(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void cleanDb() {
        GenericDao<AddressEntity> addressEntityDao = new GenericDao<>(AddressEntity.class, sessionFactory);
        GenericDao<ArtistEntity> artistEntityDao = new GenericDao<>(ArtistEntity.class, sessionFactory);
        GenericDao<AuthorEntity> authorEntityDao = new GenericDao<>(AuthorEntity.class, sessionFactory);
        GenericDao<BookEntity> bookEntityDao = new GenericDao<>(BookEntity.class, sessionFactory);
        GenericDao<CategoryEntity> categoryEntityDao = new GenericDao<>(CategoryEntity.class, sessionFactory);
        GenericDao<CdEntity> cdEntityDao = new GenericDao<>(CdEntity.class, sessionFactory);
        GenericDao<CdTitleEntity> cdTitleEntityDao = new GenericDao<>(CdTitleEntity.class, sessionFactory);
        GenericDao<CustomerEntity> customerEntityDao = new GenericDao<>(CustomerEntity.class, sessionFactory);
        GenericDao<DvdEntity> dvdEntityDao = new GenericDao<>(DvdEntity.class, sessionFactory);
        GenericDao<DvdPersonEntity> dvdPersonEntityDao = new GenericDao<>(DvdPersonEntity.class, sessionFactory);
        GenericDao<InventoryEntity> inventoryEntityDao = new GenericDao<>(InventoryEntity.class, sessionFactory);
        GenericDao<OrderEntity> orderEntityDao = new GenericDao<>(OrderEntity.class, sessionFactory);
        GenericDao<PersonEntity> personEntityDao = new GenericDao<>(PersonEntity.class, sessionFactory);
        GenericDao<ProductCategoryEntity> productCategoryEntityDao = new GenericDao<>(ProductCategoryEntity.class,
                                                                                     sessionFactory);
        GenericDao<ProductEntity> productEntityDao = new GenericDao<>(ProductEntity.class, sessionFactory);
        GenericDao<ReviewEntity> reviewEntityDao = new GenericDao<>(ReviewEntity.class, sessionFactory);
        GenericDao<StoreEntity> storeEntityDao = new GenericDao<>(StoreEntity.class, sessionFactory);
        GenericDao<TitleEntity> titleEntityDao = new GenericDao<>(TitleEntity.class, sessionFactory);

        addressEntityDao.deleteAll();
        artistEntityDao.deleteAll();
        authorEntityDao.deleteAll();
        bookEntityDao.deleteAll();
        categoryEntityDao.deleteAll();
        cdEntityDao.deleteAll();
        cdTitleEntityDao.deleteAll();
        customerEntityDao.deleteAll();
        dvdEntityDao.deleteAll();
        dvdPersonEntityDao.deleteAll();
        inventoryEntityDao.deleteAll();
        orderEntityDao.deleteAll();
        personEntityDao.deleteAll();
        productCategoryEntityDao.deleteAll();
        productEntityDao.deleteAll();
        reviewEntityDao.deleteAll();
        storeEntityDao.deleteAll();
        titleEntityDao.deleteAll();
    }
}
