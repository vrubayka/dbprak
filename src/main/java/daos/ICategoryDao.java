package daos;

import entities.CategoryEntity;

import java.util.List;

public interface ICategoryDao extends IGenericDao<CategoryEntity> {

    public List<CategoryEntity> findBySuperCategory(Long superId);
}
