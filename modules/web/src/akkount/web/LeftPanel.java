/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.web;

import akkount.entity.Account;
import akkount.service.BalanceService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Messages;
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

    protected Messages messages = AppBeans.get(Messages.class);

    public LeftPanel(MenuBar menuBar, AppWindow appWindow) {
        super(menuBar, appWindow);
    }

    @Override
    public void init(Component parent) {
        Label label = new Label(messages.getMessage(getClass(), "LeftPanel.caption"));
        label.setStyleName("cuba-folders-pane-caption");
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(label);
        outputBalance(layout);
        addComponent(layout);
        super.init(parent);
    }

    private void outputBalance(VerticalLayout layout) {
        TimeSource timeSource = AppBeans.get(TimeSource.class);
        BalanceService balanceService = AppBeans.get(BalanceService.class);

        LoadContext loadContext = new LoadContext(Account.class);
        loadContext.setQueryString("select a from akk$Account a where a.active = true order by a.name");
        List<Account> accounts = dataService.loadList(loadContext);

        if (accounts.size() > 0) {
            GridLayout grid = new GridLayout(3, accounts.size());
            grid.setMargin(true);
            grid.setSpacing(true);
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                BigDecimal balance = balanceService.getBalance(account.getId(), timeSource.currentTimestamp());
                if (BigDecimal.ZERO.compareTo(balance) != 0) {
                    grid.addComponent(new Label(account.getName()), 0, i);

                    Label sumLabel = new Label(balance.toString());
                    grid.addComponent(sumLabel, 1, i);
                    grid.setComponentAlignment(sumLabel, Alignment.MIDDLE_RIGHT);

                    grid.addComponent(new Label(account.getCurrencyCode()), 2, i);
                }
            }
            layout.addComponent(grid);
        }
    }
}
