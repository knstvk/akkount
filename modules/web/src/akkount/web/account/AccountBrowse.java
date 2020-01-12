package akkount.web.account;

import akkount.entity.Account;
import akkount.service.BalanceService;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.Set;

@UiController("akk_Account.lookup")
@UiDescriptor("account-browse.xml")
@LookupComponent("accountTable")
@LoadDataBeforeShow
public class AccountBrowse extends StandardLookup<Account> {

    @Inject
    private Table<Account> accountTable;

    @Inject
    private BalanceService balanceService;

    @Inject
    private Dialogs dialogs;

    @Inject
    private MessageBundle messageBundle;

    @Subscribe("accountTable.recalcBalance")
    public void onAccountTableRecalcBalance(Action.ActionPerformedEvent event) {
        Set<Account> selected = accountTable.getSelected();
        if (!selected.isEmpty()) {
            dialogs.createOptionDialog(Dialogs.MessageType.CONFIRMATION)
                    .withCaption(messageBundle.getMessage("recalcBalance.title"))
                    .withMessage(messageBundle.getMessage("recalcBalance.msg"))
                    .withActions(
                            new DialogAction(DialogAction.Type.OK) {
                                @Override
                                public void actionPerform(Component component) {
                                    for (Account account : selected) {
                                        balanceService.recalculateBalance(account.getId());
                                    }
                                }
                            },
                            new DialogAction(DialogAction.Type.CANCEL)
                    )
                    .show();
        }
    }
}