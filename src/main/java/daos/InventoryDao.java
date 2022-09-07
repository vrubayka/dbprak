package daos;

import entities.*;
import entities.InventoryEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;

import java.util.List;

public class InventoryDao extends GenericDao<InventoryEntity> {

    public InventoryDao(SessionFactory sessionFactory) {
        super(InventoryEntity.class, sessionFactory);
    }

    public List<InventoryEntity> findInventoryForProduct (String prod_Id){
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        SelectionQuery<InventoryEntity> query = session.createSelectionQuery(
                        "FROM InventoryEntity ie WHERE ie.prodId = :prodId", InventoryEntity.class)
                .setParameter("prodId", prod_Id);
        List<InventoryEntity> inventories = query.getResultList();
        tx.commit();
        return inventories;
        }
    }
