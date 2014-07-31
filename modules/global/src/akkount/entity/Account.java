package akkount.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Listeners;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NamePattern("%s|name")
@Table(name = "AKK_ACCOUNT")
@Entity(name = "akk$Account")
@Listeners("akkount.entitylisteners.AccountEntityListener")
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

    @Column(name = "INCLUDE_IN_TOTAL")
    protected Boolean includeInTotal = true;

    private static final long serialVersionUID = 1024314820562143050L;

    public void setIncludeInTotal(Boolean includeInTotal) {
        this.includeInTotal = includeInTotal;
    }

    public Boolean getIncludeInTotal() {
        return includeInTotal;
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