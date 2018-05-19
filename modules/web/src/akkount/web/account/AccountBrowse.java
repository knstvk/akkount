package akkount.web.account;

import akkount.entity.Account;
import akkount.service.BalanceService;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.web.AppUI;

import javax.inject.Inject;
import java.util.Set;

public class AccountBrowse extends AbstractLookup {

    @Inject
    private Table<Account> accountTable;

    @Inject
    private BalanceService balanceService;

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