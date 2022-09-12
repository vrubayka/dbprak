package middle.wrapperClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryNode {

    private Long id;
    private String name;
    private List<CategoryNode> childCategories;

    public CategoryNode(String name) {
        this(name,null);
    }

    public CategoryNode(String name, Long id) {
        this(name, id, new ArrayList<>());
    }

    public CategoryNode(String name, Long id, List<CategoryNode> childCategories) {
        this.name = name;
        this.id = id;
        this.childCategories = childCategories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryNode> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<CategoryNode> childCategories) {
        this.childCategories = childCategories;
    }

    public String getValue(){
        return StringUtils.substring(name, 0, 10);
    }

}
