package akkount.service;

import akkount.entity.Balance;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service(BalanceService.NAME)
public class BalanceServiceBean implements BalanceService {

    @Inject
    protected Persistence persistence;

    @Override
    @Transactional
    public BigDecimal getBalance(UUID accountId, Date date) {
        EntityManager em = persistence.getEntityManager();

        TypedQuery<Balance> balQuery = em.createQuery(
                "select b from akk$Balance b where b.account.id = ?1 and b.balanceDate < ?2 order by b.balanceDate desc",
                Balance.class);
        balQuery.setParameter(1, accountId);
        balQuery.setParameter(2, date);
        balQuery.setMaxResults(1);
        Balance startBalance = balQuery.getFirstResult();
        BigDecimal startAmount = startBalance != null ? startBalance.getAmount() : BigDecimal.ZERO;

        String expenseQueryStr = "select sum(o.amount1) from akk$Operation o where o.acc1.id = ?1 and o.opDate <= ?2";
        if (startBalance != null)
            expenseQueryStr += " and o.opDate >= ?3";
        Query expenseQuery = em.createQuery(expenseQueryStr);
        expenseQuery.setParameter(1, accountId);
        expenseQuery.setParameter(2, date);
        if (startBalance != null)
            expenseQuery.setParameter(3, startBalance.getBalanceDate());
        BigDecimal expense = (BigDecimal) expenseQuery.getSingleResult();
        if (expense == null)
            expense = BigDecimal.ZERO;

        String incomeQueryStr = "select sum(o.amount2) from akk$Operation o where o.acc2.id = ?1 and o.opDate <= ?2";
        if (startBalance != null)
            incomeQueryStr += " and o.opDate >= ?3";
        Query incomeQuery = em.createQuery(incomeQueryStr);
        incomeQuery.setParameter(1, accountId);
        incomeQuery.setParameter(2, date);
        if (startBalance != null)
            incomeQuery.setParameter(3, startBalance.getBalanceDate());
        BigDecimal income = (BigDecimal) incomeQuery.getSingleResult();
        if (income == null)
            income = BigDecimal.ZERO;

        return startAmount.add(income).subtract(expense);
    }
}