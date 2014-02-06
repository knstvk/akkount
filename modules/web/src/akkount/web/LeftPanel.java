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
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.apache.commons.lang.BooleanUtils;

import java.math.BigDecimal;
import java.util.*;

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

            balanceGrid = new GridLayout(3, totals.size() + accounts.size() + 3);
            balanceGrid.setMargin(true);
            balanceGrid.setSpacing(true);

            DecimalFormatter formatter = new DecimalFormatter();

            int row = 0;
            if (!totals.isEmpty()) {
                for (Map.Entry<String, BigDecimal> entry : totals.entrySet()) {
                    Label sumLabel = new Label(formatter.format(entry.getValue()));
                    sumLabel.setStyleName("h2");
                    balanceGrid.addComponent(sumLabel, 1, row);
                    balanceGrid.setComponentAlignment(sumLabel, Alignment.MIDDLE_RIGHT);

                    Label currencyLabel = new Label(entry.getKey());
                    currencyLabel.setStyleName("h2");
                    balanceGrid.addComponent(currencyLabel, 2, row);

                    row++;
                }
            }

            List<Account> includedAccounts = new ArrayList<>();
            List<Account> excludedAccounts = new ArrayList<>();
            for (Account account : balances.keySet()) {
                if (BooleanUtils.isTrue(account.getIncludeInTotal()))
                    includedAccounts.add(account);
                else
                    excludedAccounts.add(account);
            }

            Label label = new Label("<br/>");
            label.setContentMode(ContentMode.HTML);
            balanceGrid.addComponent(label, 0, row++);
            for (Account account : includedAccounts) {
                addAccountBalance(account, balances.get(account), formatter, row);
                row++;
            }
            if (!excludedAccounts.isEmpty()) {
                label = new Label("<br/>");
                label.setContentMode(ContentMode.HTML);
                balanceGrid.addComponent(label, 0, row++);
                for (Account account : excludedAccounts) {
                    addAccountBalance(account, balances.get(account), formatter, row);
                    row++;
                }
            }

            balanceLayout.addComponent(balanceGrid);
        }
    }

    private void addAccountBalance(Account account, BigDecimal balance, DecimalFormatter formatter, int row) {
        balanceGrid.addComponent(new Label(account.getName()), 0, row);

        Label sumLabel = new Label(formatter.format(balance));
        balanceGrid.addComponent(sumLabel, 1, row);
        balanceGrid.setComponentAlignment(sumLabel, Alignment.MIDDLE_RIGHT);

        balanceGrid.addComponent(new Label(account.getCurrencyCode()), 2, row);
    }
}
