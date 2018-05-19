package akkount;

import akkount.entity.Balance;
import akkount.event.BalanceChangedEvent;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.PersistenceTools;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.listener.BeforeCommitTransactionListener;
import com.haulmont.cuba.core.sys.PersistenceImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Component("akk_BalanceTransactionListener")
public class BalanceTransactionListener implements BeforeCommitTransactionListener {

    @Inject
    private Persistence persistence;

    @Inject
    private PersistenceTools persistenceTools;

    @Inject
    private Events events;

    @Override
    public void beforeCommit(EntityManager entityManager, Collection<Entity> managedEntities) {
        boolean changed = managedEntities.stream()
                .anyMatch(entity -> entity instanceof Balance && persistenceTools.isDirty(entity));
        if (changed) {
            List<Consumer<Integer>> runAfterCompletion = persistence.getEntityManagerContext().getAttribute(PersistenceImpl.RUN_AFTER_COMPLETION_ATTR);
            if (runAfterCompletion == null) {
                runAfterCompletion = new ArrayList<>();
                persistence.getEntityManagerContext().setAttribute(PersistenceImpl.RUN_AFTER_COMPLETION_ATTR, runAfterCompletion);
            }
            runAfterCompletion.add((txStatus) -> {
                if (TransactionSynchronization.STATUS_COMMITTED == txStatus) {
                    events.publish(new BalanceChangedEvent(this));
                }
            });
        }
    }
}