package akkount.service;

import akkount.entity.Category;
import akkount.entity.CategoryAmount;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@Service(ReportService.NAME)
public class ReportServiceBean implements ReportService {

    @Inject
    protected Persistence persistence;

    @Override
    @Transactional
    public List<CategoryAmount> getTurnoverByCategories(Date fromDate, Date toDate) {
        List<CategoryAmount> list = new ArrayList<>();

        EntityManager em = persistence.getEntityManager();
        TypedQuery<Category> catQuery = em.createQuery("select c from akk$Category c", Category.class);
        for (Category category : catQuery.getResultList()) {
            Query amountQuery = em.createQuery(
                    "select sum(o.amount1), sum(o.amount2) from akk$Operation o " +
                    "where (o.opDate between ?1 and ?2) and o.category.id = ?3");
            amountQuery.setParameter(1, fromDate)
                    .setParameter(2, toDate)
                    .setParameter(3, category.getId());
            Object[] res = (Object[]) amountQuery.getFirstResult();
            if (res != null) {
                BigDecimal a1 = res[0] == null ? BigDecimal.ZERO : (BigDecimal) res[0];
                BigDecimal a2 = res[1] == null ? BigDecimal.ZERO : (BigDecimal) res[1];
                BigDecimal amount = a2.subtract(a1);
                if (amount.compareTo(BigDecimal.ZERO) != 0) {
                    CategoryAmount categoryAmount = new CategoryAmount();
                    categoryAmount.setCategory(category);
                    categoryAmount.setAmount(amount);
                    list.add(categoryAmount);
                }
            }
        }

        Collections.sort(list, new Comparator<CategoryAmount>() {
            @Override
            public int compare(CategoryAmount o1, CategoryAmount o2) {
                return o2.getAmount().compareTo(o1.getAmount());
            }
        });
        return list;
    }
}