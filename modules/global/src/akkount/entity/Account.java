package akkount.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;

import javax.persistence.*;

@NamePattern("%s|name")
@Table(name = "AKK_ACCOUNT")
@Entity(name = "akk_Account")
@PublishEntityChangedEvents
public class Account extends StandardEntity {
    @Column(name = "NAME", nullable = false, length = 20, unique = true)
    protected String name;

    @Column(name = "DESCRIPTION", length = 100)
    protected String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CURRENCY_ID")
    protected Currency currency;

    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    protected String currencyCode;

    @Column(name = "ACTIVE")
    protected Boolean active = true;

    @Column(name = "GROUP_")
    protected Integer group;

    private static final long serialVersionUID = 1024314820562143050L;

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }


    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }


    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }


    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}