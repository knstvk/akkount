/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.web;

import akkount.entity.Account;
import akkount.service.BalanceService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.app.folders.FoldersPane;
import com.vaadin.ui.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LeftPanel extends FoldersPane {

    private VerticalLayout balanceLayout;
    private GridLayout balanceGrid;

    public LeftPanel(MenuBar menuBar, AppWindow appWindow) {
        super(menuBar, appWindow);
    }

    @Override
    public void init(Component parent) {
        Label label = new Label(messages.getMessage(getClass(), "LeftPanel.caption"));
        label.setStyleName("cuba-folders-pane-caption");
        balanceLayout = new VerticalLayout();
        balanceLayout.setMargin(true);
        balanceLayout.setSpacing(true);
        balanceLayout.addComponent(label);
        addComponent(balanceLayout);

        refreshBalance();

        super.init(parent);
    }

    public void refreshBalance() {
        TimeSource timeSource = AppBeans.get(TimeSource.class);
        BalanceService balanceService = AppBeans.get(BalanceService.class);

        LoadContext loadContext = new LoadContext(Account.class);
        loadContext.setQueryString("select a from akk$Account a where a.active = true order by a.name");
        List<Account> accounts = dataService.loadList(loadContext);

        if (balanceGrid != null) {
            balanceLayout.removeComponent(balanceGrid);
        }

        if (accounts.size() > 0) {
            balanceGrid = new GridLayout(3, accounts.size());
            balanceGrid.setMargin(true);
            balanceGrid.setSpacing(true);
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                BigDecimal balance = balanceService.getBalance(account.getId(), timeSource.currentTimestamp());
                if (BigDecimal.ZERO.compareTo(balance) != 0) {
                    balanceGrid.addComponent(new Label(account.getName()), 0, i);

                    Label sumLabel = new Label(balance.toString());
                    balanceGrid.addComponent(sumLabel, 1, i);
                    balanceGrid.setComponentAlignment(sumLabel, Alignment.MIDDLE_RIGHT);

                    balanceGrid.addComponent(new Label(account.getCurrencyCode()), 2, i);
                }
            }
            balanceLayout.addComponent(balanceGrid);
        }
    }
}
