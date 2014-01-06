/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.service;

import akkount.entity.Balance;
import com.haulmont.cuba.core.*;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(BalanceWorker.NAME)
public class BalanceWorker {

    public static final String NAME = "akk_BalanceWorker";

    @Inject
    protected Persistence persistence;

    public BigDecimal getBalance(final UUID accountId, final Date date) {
        return persistence.createTransaction().execute(new Transaction.Callable<BigDecimal>() {
            @Override
            public BigDecimal call(EntityManager em) {
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
        });
    }
}
