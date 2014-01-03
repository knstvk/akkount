package akkount.gui.operation;

import java.util.Calendar;
import java.util.Map;

import akkount.entity.OperationType;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.*;
import akkount.entity.Operation;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.time.DateUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

public class OperationEdit extends AbstractEditor<Operation> {

    @Inject
    protected TabSheet tabSheet;

    @Named("expenseFrame.account1Field")
    protected LookupPickerField account1Field;

    @Named("expenseFrame.amount1Field")
    protected TextField amount1Field;

    @Named("expenseFrame.currency1Lab")
    protected Label currency1Lab;

    @Named("incomeFrame.account2Field")
    protected LookupPickerField account2Field;

    @Named("incomeFrame.amount2Field")
    protected TextField amount2Field;

    @Named("incomeFrame.currency2Lab")
    protected Label currency2Lab;

    @Named("transferFrame.account1Field")
    protected LookupPickerField transAccount1Field;

    @Named("transferFrame.amount1Field")
    protected TextField transAmount1Field;

    @Named("transferFrame.currency1Lab")
    protected Label transCurrency1Lab;

    @Named("transferFrame.account2Field")
    protected LookupPickerField transAccount2Field;

    @Named("transferFrame.amount2Field")
    protected TextField transAmount2Field;

    @Named("transferFrame.currency2Lab")
    protected Label transCurrency2Lab;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected Datasource<Operation> operationDs;

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

        operationDs.addListener(new DsListenerAdapter<Operation>() {
            @Override
            public void valueChanged(Operation source, String property, @Nullable Object prevValue, @Nullable Object value) {
                if ("acc1".equals(property)) {
                    String currency = source.getAcc1() != null ? source.getAcc1().getCurrencyCode() : "";
                    currency1Lab.setValue(currency);
                    transCurrency1Lab.setValue(currency);
                }
                if ("acc2".equals(property)) {
                    String currency = source.getAcc2() != null ? source.getAcc2().getCurrencyCode() : "";
                    currency2Lab.setValue(currency);
                    transCurrency2Lab.setValue(currency);
                }
                if ("opType".equals(property)) {
                    OperationType opType = (OperationType) value;
                    if (opType != null) {
                        account1Field.setRequired(opType == OperationType.EXPENSE);
                        amount1Field.setRequired(opType == OperationType.EXPENSE);
                        account2Field.setRequired(opType == OperationType.INCOME);
                        amount2Field.setRequired(opType == OperationType.INCOME);
                        transAccount1Field.setRequired(opType == OperationType.TRANSFER);
                        transAmount1Field.setRequired(opType == OperationType.TRANSFER);
                        transAccount2Field.setRequired(opType == OperationType.TRANSFER);
                        transAmount2Field.setRequired(opType == OperationType.TRANSFER);
                    }
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