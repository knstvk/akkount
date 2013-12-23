package akkount.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|name")
@Table(name = "AKK_CATEGORY")
@Entity(name = "akk$Category")
public class Category extends StandardEntity {
    @Column(name = "NAME", nullable = false, length = 50)
    protected String name;

    @Column(name = "DESCRIPTION", length = 100)
    protected String description;

    private static final long serialVersionUID = 5768212165913475729L;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}