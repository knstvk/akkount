package akkount.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|code")
@Table(name = "AKK_CURRENCY")
@Entity(name = "akk$Currency")
public class Currency extends StandardEntity {
    @Column(name = "CODE", nullable = false, length = 3)
    protected String code;

    @Column(name = "NAME", length = 50)
    protected String name;

    private static final long serialVersionUID = -3270758636888264613L;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}