package akkount.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.haulmont.cuba.core.entity.StandardEntity;

@Table(name = "AKK_BALANCE")
@Entity(name = "akk_Balance")
public class Balance extends StandardEntity {
    private static final long serialVersionUID = 918143020139005638L;

    @Temporal(TemporalType.DATE)
    @Column(name = "BALANCE_DATE", nullable = false)
    protected Date balanceDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ACCOUNT_ID")
    protected Account account;

    @Column(name = "AMOUNT")
    protected BigDecimal amount = BigDecimal.ZERO;

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }


}