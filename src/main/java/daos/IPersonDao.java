package daos;

import entities.PersonEntity;

public interface IPersonDao extends IGenericDao<PersonEntity> {

    PersonEntity findByName(String name);

}
