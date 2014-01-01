package akkount.gui.operation;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import akkount.entity.OperationType;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.AbstractEditor;
import akkount.entity.Operation;
import com.haulmont.cuba.gui.components.TabSheet;
import org.apache.commons.lang.time.DateUtils;

import javax.inject.Inject;

public class OperationEdit extends AbstractEditor<Operation> {

    @Inject
    protected TabSheet tabSheet;

    @Inject
    protected TimeSource timeSource;

    @Override
    public void init(Map<String, Object> params) {
        tabSheet.addListener(new TabSheet.TabChangeListener() {
            @Override
            public void tabChanged(TabSheet.Tab newTab) {
                switch (newTab.getName()) {
                    case "expenseTab":
                        getItem().setOpType(OperationType.EXPENSE);
                        break;
                    case "incomeTab":
                        getItem().setOpType(OperationType.INCOME);
                        break;
                    case "transferTab":
                        getItem().setOpType(OperationType.TRANSFER);
                        break;
                    case "debtTab":
                        getItem().setOpType(OperationType.DEBT);
                        break;
                    default:
                        throw new DevelopmentException("Invalid tab id: " + newTab.getName());
                }
            }
        });
    }

    @Override
    protected void initItem(Operation item) {
        if (PersistenceHelper.isNew(item)) {
            item.setOpType(OperationType.EXPENSE);
            item.setOpDate(DateUtils.truncate(timeSource.currentTimestamp(), Calendar.DAY_OF_MONTH));
        }
    }
}