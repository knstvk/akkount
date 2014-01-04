package akkount.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.security.entity.User;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import javax.persistence.Column;

@Table(name = "AKK_USER_DATA")
@Entity(name = "akk$UserData")
public class UserData extends BaseUuidEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    protected User user;

    @Column(name = "KEY_", length = 50, nullable = false)
    protected String key;

    @Column(name = "VALUE_", length = 500)
    protected String value;

    private static final long serialVersionUID = 3110000356480013115L;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }


}