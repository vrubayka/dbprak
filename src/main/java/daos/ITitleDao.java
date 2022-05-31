package daos;

import entities.TitleEntity;

public interface ITitleDao extends IGenericDao<TitleEntity> {

    TitleEntity findByName(String name);
}
