package akkount.web.account;

import akkount.entity.Account;
import akkount.service.BalanceService;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.web.AppUI;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.Set;

public class AccountBrowse extends AbstractLookup {

    @Named("accountTable.create")
    private CreateAction createAction;

    @Named("accountTable.edit")
    private EditAction editAction;

    @Inject
    private Table<Account> accountTable;

    @Inject
    private BalanceService balanceService;

    @Override
    public void init(Map<String, Object> params) {
        createAction.setOpenType(WindowManager.OpenType.DIALOG);
        editAction.setOpenType(WindowManager.OpenType.DIALOG);
    }

    public void onRecalcBalance(Component source) {
        final Set<Account> selected = accountTable.getSelected();
        if (!selected.isEmpty()) {
            showOptionDialog(
                    getMessage("recalcBalance.title"),
                    getMessage("recalcBalance.msg"),
                    MessageType.CONFIRMATION,
                    new Action[] {
                            new DialogAction(DialogAction.Type.OK) {
                                @Override
                                public void actionPerform(Component component) {
                                    for (Account account : selected) {
                                        balanceService.recalculateBalance(account.getId());
                                    }
                                    ((akkount.web.MainWindow) AppUI.getCurrent().getTopLevelWindow()).refreshBalance();
                                }
                            },
                            new DialogAction(DialogAction.Type.CANCEL)
                    }
            );
        }
    }
}