package akkount.web.operation;

import akkount.entity.Operation;
import akkount.entity.OperationType;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;

public class OperationBrowse extends AbstractLookup {

    @Inject
    protected Table<Operation> operationTable;

    @Inject
    protected Button createExpenseBtn;

    @Inject
    protected Button createIncomeBtn;

    @Inject
    protected Button createTransferBtn;

    @Override
    public void init(Map<String, Object> params) {
        OperationCreateAction createExpenseAction = new OperationCreateAction(OperationType.EXPENSE);
        operationTable.addAction(createExpenseAction);
        createExpenseBtn.setAction(createExpenseAction);

        OperationCreateAction createIncomeAction = new OperationCreateAction(OperationType.INCOME);
        operationTable.addAction(createIncomeAction);
        createIncomeBtn.setAction(createIncomeAction);

        OperationCreateAction createTransferAction = new OperationCreateAction(OperationType.TRANSFER);
        operationTable.addAction(createTransferAction);
        createTransferBtn.setAction(createTransferAction);

        operationTable.addAction(new OperationEditAction());
    }

    protected class OperationCreateAction extends CreateAction {

        public OperationCreateAction(OperationType opType) {
            super(OperationBrowse.this.operationTable, WindowManager.OpenType.NEW_TAB, opType.name());
            setInitialValues(Collections.<String, Object>singletonMap("opType", opType));
            setCaption(messages.getMessage(opType));
            setShortcut("Ctrl-Shift-Key" + String.valueOf(opType.ordinal() + 1));
        }

        @Override
        protected void afterCommit(Entity entity) {
            operationTable.getDatasource().refresh();
        }
    }

    protected class OperationEditAction extends EditAction {

        public OperationEditAction() {
            super(OperationBrowse.this.operationTable, WindowManager.OpenType.NEW_TAB);
        }

        @Override
        protected void afterCommit(Entity entity) {
            operationTable.getDatasource().refresh();
        }
    }
}