package entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "category", schema = "public", catalog = "dbprak")
public class CategoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    @Basic
    @Column(name = "category_name", nullable = false)
    private String categoryName;
    @Basic
    @Column(name = "super_category")
    private Long superCategory;
    @ManyToOne
    @JoinColumn(name = "super_category", referencedColumnName = "category_id", insertable = false, updatable = false)
    private CategoryEntity categoryBySuperCategory;
    @OneToMany(mappedBy = "categoryBySuperCategory")
    private Collection<CategoryEntity> categoriesByCategoryId;
    @OneToMany(mappedBy = "categoryByCategoryId")
    private Collection<ProductCategoryEntity> productCategoriesByCategoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getSuperCategory() {
        return superCategory;
    }

    public void setSuperCategory(Long superCategory) {
        this.superCategory = superCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        return Objects.equals(categoryId, that.categoryId) && Objects.equals(categoryName, that.categoryName) &&
               Objects.equals(superCategory, that.superCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, categoryName, superCategory);
    }

    public CategoryEntity getCategoryBySuperCategory() {
        return categoryBySuperCategory;
    }

    public void setCategoryBySuperCategory(CategoryEntity categoryBySuperCategory) {
        this.categoryBySuperCategory = categoryBySuperCategory;
    }

    public Collection<CategoryEntity> getCategoriesByCategoryId() {
        return categoriesByCategoryId;
    }

    public void setCategoriesByCategoryId(Collection<CategoryEntity> categoriesByCategoryId) {
        this.categoriesByCategoryId = categoriesByCategoryId;
    }

    public Collection<ProductCategoryEntity> getProductCategoriesByCategoryId() {
        return productCategoriesByCategoryId;
    }

    public void setProductCategoriesByCategoryId(
            Collection<ProductCategoryEntity> productCategoriesByCategoryId) {
        this.productCategoriesByCategoryId = productCategoriesByCategoryId;
    }
}
