package akkount.web.operation;

import akkount.entity.Operation;
import akkount.entity.OperationType;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("akk_Operation.lookup")
@UiDescriptor("operation-browse.xml")
@LookupComponent("operationTable")
@LoadDataBeforeShow
public class OperationBrowse extends StandardLookup<Operation> {

    @Inject
    protected Table<Operation> operationTable;

    @Inject
    protected Button createExpenseBtn;

    @Inject
    protected Button createIncomeBtn;

    @Inject
    protected Button createTransferBtn;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private CollectionLoader<Operation> operationsDl;

    @Subscribe("operationTable.createExpense")
    private void onOperationTableCreateExpense(Action.ActionPerformedEvent event) {
        openEditorForCreate(OperationType.EXPENSE);
    }

    @Subscribe("operationTable.createIncome")
    private void onOperationTableCreateIncome(Action.ActionPerformedEvent event) {
        openEditorForCreate(OperationType.INCOME);
    }

    @Subscribe("operationTable.createTransfer")
    private void onOperationTableCreateTransfer(Action.ActionPerformedEvent event) {
        openEditorForCreate(OperationType.TRANSFER);
    }

    private void openEditorForCreate(OperationType operationType) {
        Screen editor = screenBuilders.editor(operationTable)
                .newEntity()
                .withInitializer(operation -> operation.setOpType(operationType))
                .build();
        editor.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.getCloseAction().equals(WINDOW_COMMIT_AND_CLOSE_ACTION)) {
                operationsDl.load();
            }
        });
        editor.show();
    }

    @Subscribe("operationTable.edit")
    private void onOperationTableEdit(Action.ActionPerformedEvent event) {
        Screen editor = screenBuilders.editor(operationTable).build();
        editor.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.getCloseAction().equals(WINDOW_COMMIT_AND_CLOSE_ACTION)) {
                operationsDl.load();
            }
        });
        editor.show();
    }


//    @Override
//    public void init(Map<String, Object> params) {
//        OperationCreateAction createExpenseAction = new OperationCreateAction(OperationType.EXPENSE);
//        operationTable.addAction(createExpenseAction);
//        createExpenseBtn.setAction(createExpenseAction);
//
//        OperationCreateAction createIncomeAction = new OperationCreateAction(OperationType.INCOME);
//        operationTable.addAction(createIncomeAction);
//        createIncomeBtn.setAction(createIncomeAction);
//
//        OperationCreateAction createTransferAction = new OperationCreateAction(OperationType.TRANSFER);
//        operationTable.addAction(createTransferAction);
//        createTransferBtn.setAction(createTransferAction);
//
//        operationTable.addAction(new OperationEditAction());
//    }

//    protected class OperationCreateAction extends CreateAction {
//
//        public OperationCreateAction(OperationType opType) {
//            super(OperationBrowse.this.operationTable, WindowManager.OpenType.NEW_TAB, opType.name());
//            setInitialValues(Collections.<String, Object>singletonMap("opType", opType));
//            setCaption(messages.getMessage(opType));
//            setShortcut("Ctrl-Shift-Key" + String.valueOf(opType.ordinal() + 1));
//        }
//
//        @Override
//        protected void afterCommit(Entity entity) {
//            operationTable.getDatasource().refresh();
//        }
//    }
//
//    protected class OperationEditAction extends EditAction {
//
//        public OperationEditAction() {
//            super(OperationBrowse.this.operationTable, WindowManager.OpenType.NEW_TAB);
//        }
//
//        @Override
//        protected void afterCommit(Entity entity) {
//            operationTable.getDatasource().refresh();
//        }
//    }
}