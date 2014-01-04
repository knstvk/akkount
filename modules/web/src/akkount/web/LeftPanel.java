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
import org.apache.commons.lang.BooleanUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
            Map<Account, BigDecimal> balances = new LinkedHashMap<>();
            for (Account account : accounts) {
                BigDecimal balance = balanceService.getBalance(account.getId(), timeSource.currentTimestamp());
                if (BigDecimal.ZERO.compareTo(balance) != 0)
                    balances.put(account, balance);
            }

            Map<String, BigDecimal> totals = new TreeMap<>();
            for (Map.Entry<Account, BigDecimal> entry : balances.entrySet()) {
                if (BooleanUtils.isTrue(entry.getKey().getIncludeInTotal())) {
                    BigDecimal total = totals.get(entry.getKey().getCurrencyCode());
                    if (total == null)
                        total = entry.getValue();
                    else
                        total = total.add(entry.getValue());
                    totals.put(entry.getKey().getCurrencyCode(), total);
                }
            }

            balanceGrid = new GridLayout(3, totals.size() + accounts.size() + (totals.isEmpty() ? 1 : 2));
            balanceGrid.setMargin(true);
            balanceGrid.setSpacing(true);

            int row = 0;
            if (!totals.isEmpty()) {
                balanceGrid.addComponent(new Label(messages.getMessage(getClass(), "LeftPanel.total")), 0, row++);
                for (Map.Entry<String, BigDecimal> entry : totals.entrySet()) {
                    Label sumLabel = new Label(entry.getValue().toString());
                    sumLabel.setStyleName("h2");
                    balanceGrid.addComponent(sumLabel, 1, row);
                    balanceGrid.setComponentAlignment(sumLabel, Alignment.MIDDLE_RIGHT);

                    Label currencyLabel = new Label(entry.getKey());
                    currencyLabel.setStyleName("h2");
                    balanceGrid.addComponent(currencyLabel, 2, row);

                    row++;
                }
            }

            balanceGrid.addComponent(new Label(messages.getMessage(getClass(), "LeftPanel.accounts")), 0, row++);
            for (Map.Entry<Account, BigDecimal> entry : balances.entrySet()) {
                balanceGrid.addComponent(new Label(entry.getKey().getName()), 0, row);

                Label sumLabel = new Label(entry.getValue().toString());
                balanceGrid.addComponent(sumLabel, 1, row);
                balanceGrid.setComponentAlignment(sumLabel, Alignment.MIDDLE_RIGHT);

                balanceGrid.addComponent(new Label(entry.getKey().getCurrencyCode()), 2, row);

                row++;
            }
            balanceLayout.addComponent(balanceGrid);
        }
    }
}
