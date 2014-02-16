/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.web.operation;

import akkount.entity.Account;
import akkount.entity.Operation;
import akkount.entity.OperationType;
import akkount.service.UserDataKeys;
import akkount.service.UserDataService;
import akkount.web.App;
import akkount.web.LeftPanel;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.data.DsContext;
import org.apache.commons.lang.time.DateUtils;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
 */
public class OperationEdit extends AbstractEditor<Operation> {

    @Inject
    protected GroupBoxLayout frameContainer;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected UserDataService userDataService;

    private OperationFrame operationFrame;

    @Override
    public void init(Map<String, Object> params) {
        Operation operation = WindowParams.ITEM.getEntity(params);
        if (operation.getOpType() == null)
            operation.setOpType(OperationType.EXPENSE);

        String frameId = operation.getOpType().name().toLowerCase() + "-frame";

        operationFrame = openFrame(frameContainer, frameId, params);

        frameContainer.setCaption(messages.getMessage(operation.getOpType()));

        getDsContext().addListener(new DsContext.CommitListenerAdapter() {
            @Override
            public void afterCommit(CommitContext context, Set<Entity> result) {
                LeftPanel leftPanel = App.getLeftPanel();
                if (leftPanel != null)
                    leftPanel.refreshBalance();
            }
        });
    }

    @Override
    protected void initNewItem(Operation item) {
        item.setOpDate(DateUtils.truncate(timeSource.currentTimestamp(), Calendar.DAY_OF_MONTH));
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

    private Account loadAccount(String key) {
        return userDataService.loadEntity(key, Account.class);
    }

}
