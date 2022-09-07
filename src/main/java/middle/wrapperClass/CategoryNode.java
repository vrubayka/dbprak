package middle.wrapperClass;

import java.util.ArrayList;
import java.util.List;

public class CategoryNode {

    private Long id;
    private List<CategoryNode> childCategories;

    public CategoryNode() {
        this(null);
    }

    public CategoryNode(Long id) {
        this(id, new ArrayList<>());
    }

    public CategoryNode(Long id, List<CategoryNode> childCategories) {
        this.id = id;
        this.childCategories = childCategories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CategoryNode> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<CategoryNode> childCategories) {
        this.childCategories = childCategories;
    }
}
