package akkount.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Table(name = "AKK_OPERATION")
@Entity(name = "akk$Operation")
@Listeners("akkount.core.entitylisteners.OperationEntityListener")
public class Operation extends StandardEntity {
    @Column(name = "OP_TYPE", nullable = false)
    protected String opType;

    @Temporal(TemporalType.DATE)
    @Column(name = "OP_DATE", nullable = false)
    protected Date opDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACC1_ID")
    protected Account acc1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACC2_ID")
    protected Account acc2;

    @Column(name = "AMOUNT1")
    protected BigDecimal amount1 = BigDecimal.ZERO;

    @Column(name = "AMOUNT2")
    protected BigDecimal amount2 = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    protected Category category;

    @Column(name = "COMMENTS", length = 200)
    protected String comments;

    private static final long serialVersionUID = 7040817103549067673L;

    public void setOpType(OperationType opType) {
        this.opType = opType == null ? null : opType.getId();
    }

    public OperationType getOpType() {
        return opType == null ? null : OperationType.fromId(opType);
    }

    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }

    public Date getOpDate() {
        return opDate;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }


    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }


    public void setAcc1(Account acc1) {
        this.acc1 = acc1;
    }

    public Account getAcc1() {
        return acc1;
    }

    public void setAcc2(Account acc2) {
        this.acc2 = acc2;
    }

    public Account getAcc2() {
        return acc2;
    }

    public void setAmount1(BigDecimal amount1) {
        this.amount1 = amount1;
    }

    public BigDecimal getAmount1() {
        return amount1;
    }

    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

    public BigDecimal getAmount2() {
        return amount2;
    }

}