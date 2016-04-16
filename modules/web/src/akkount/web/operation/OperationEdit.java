package akkount.web.operation;

import akkount.entity.Account;
import akkount.entity.Operation;
import akkount.entity.OperationType;
import akkount.service.UserDataKeys;
import akkount.service.UserDataService;
import akkount.web.App;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.time.DateUtils;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class OperationEdit extends AbstractEditor<Operation> {

    public static final String LAST_OPERATION_DATE_ATTR = "lastOperationDate";

    @Inject
    private GroupBoxLayout frameContainer;

    @Inject
    private TimeSource timeSource;

    @Inject
    private UserDataService userDataService;

    @Inject
    private UserSession userSession;

    private OperationFrame operationFrame;

    @Override
    public void init(Map<String, Object> params) {
        Operation operation = (Operation) WindowParams.ITEM.getEntity(params);
        if (operation.getOpType() == null)
            operation.setOpType(OperationType.EXPENSE);

        String frameId = operation.getOpType().name().toLowerCase() + "-frame";

        operationFrame = (OperationFrame) openFrame(frameContainer, frameId, params);

        frameContainer.setCaption(messages.getMessage(operation.getOpType()));

        getDsContext().addAfterCommitListener((context, result) -> {
            ((App) App.getInstance()).getMainWindow().refreshBalance();
        });
    }

    @Override
    protected void initNewItem(Operation item) {
        item.setOpDate(loadDate());
        switch (item.getOpType()) {
            case EXPENSE:
                item.setAcc1(loadAccount(UserDataKeys.OP_EXPENSE_ACCOUNT));
                break;
            case INCOME:
                item.setAcc2(loadAccount(UserDataKeys.OP_INCOME_ACCOUNT));
                break;
            case TRANSFER:
                item.setAcc1(loadAccount(UserDataKeys.OP_TRANSFER_EXPENSE_ACCOUNT));
                item.setAcc2(loadAccount(UserDataKeys.OP_TRANSFER_INCOME_ACCOUNT));
                break;
        }
    }

    @Override
    protected void postInit() {
        operationFrame.postInit(getItem());
    }

    @Override
    protected void postValidate(ValidationErrors errors) {
        operationFrame.postValidate(errors);
    }

    @Override
    protected boolean postCommit(boolean committed, boolean close) {
        userSession.setAttribute(LAST_OPERATION_DATE_ATTR, getItem().getOpDate());
        return true;
    }

    private Account loadAccount(String key) {
        return userDataService.loadEntity(key, Account.class);
    }

    private Date loadDate() {
        Date date = userSession.getAttribute(LAST_OPERATION_DATE_ATTR);
        return date != null ? date : DateUtils.truncate(timeSource.currentTimestamp(), Calendar.DAY_OF_MONTH);
    }

}
