package akkount.web;

import akkount.entity.Account;
import akkount.event.BalanceChangedEvent;
import akkount.service.BalanceService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.mainwindow.AppMenu;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

public class MainWindow extends AbstractMainWindow {

    private static final Logger log = LoggerFactory.getLogger(MainWindow.class);

    @Inject
    protected AppMenu mainMenu;
    @Inject
    protected Embedded logoImage;
    @Inject
    private BoxLayout balanceLayout;

    @Inject
    private DataSupplier dataSupplier;
    @Inject
    private ComponentsFactory componentsFactory;

    private GridLayout balanceGrid;

    @Override
    public void init(Map<String, Object> params) {
        mainMenu.requestFocus();
        logoImage.setSource("theme://" + messages.getMainMessage("application.logoImage"));

        refreshBalance();
    }

    @EventListener(BalanceChangedEvent.class)
    public void refreshBalance() {
        log.info("Refreshing balance");
        TimeSource timeSource = AppBeans.get(TimeSource.class);
        BalanceService balanceService = AppBeans.get(BalanceService.class);

        List<Account> accounts = dataSupplier.loadList(
                LoadContext.create(Account.class).setQuery(
                        LoadContext.createQuery("select a from akk_Account a where a.active = true order by a.name")));

        if (balanceGrid != null) {
            balanceLayout.remove(balanceGrid);
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

            balanceGrid = componentsFactory.createComponent(GridLayout.class);
            balanceGrid.setColumns(3);
            balanceGrid.setRows(totals.size() + accounts.size() + 3);
            balanceGrid.setMargin(true);
            balanceGrid.setSpacing(true);

            DecimalFormatter formatter = new DecimalFormatter();

            int row = 0;
            if (!totals.isEmpty()) {
                for (Map.Entry<String, BigDecimal> entry : totals.entrySet()) {
                    Label sumLabel = componentsFactory.createComponent(Label.class);
                    sumLabel.setValue(formatter.format(entry.getValue()));
                    sumLabel.setStyleName("totals");
                    sumLabel.setAlignment(Alignment.MIDDLE_RIGHT);
                    balanceGrid.add(sumLabel, 1, row);

                    Label currencyLabel = componentsFactory.createComponent(Label.class);
                    currencyLabel.setValue(entry.getKey());
                    currencyLabel.setStyleName("totals");
                    balanceGrid.add(currencyLabel, 2, row);

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

            Label label = componentsFactory.createComponent(Label.class);
            label.setValue("<br/>");
            label.setHtmlEnabled(true);
            balanceGrid.add(label, 0, row++);
            for (Account account : includedAccounts) {
                addAccountBalance(account, balances.get(account), formatter, row);
                row++;
            }
            if (!excludedAccounts.isEmpty()) {
                label = componentsFactory.createComponent(Label.class);
                label.setValue("<br/>");
                label.setHtmlEnabled(true);
                balanceGrid.add(label, 0, row++);
                for (Account account : excludedAccounts) {
                    addAccountBalance(account, balances.get(account), formatter, row);
                    row++;
                }
            }

            balanceLayout.add(balanceGrid);
        }
    }

    private void addAccountBalance(Account account, BigDecimal balance, DecimalFormatter formatter, int row) {
        Label label = componentsFactory.createComponent(Label.class);
        label.setValue(account.getName());
        balanceGrid.add(label, 0, row);

        Label sumLabel = componentsFactory.createComponent(Label.class);
        sumLabel.setValue(formatter.format(balance));
        sumLabel.setAlignment(Alignment.MIDDLE_RIGHT);
        balanceGrid.add(sumLabel, 1, row);

        Label curlabel = componentsFactory.createComponent(Label.class);
        curlabel.setValue(account.getCurrencyCode());
        balanceGrid.add(curlabel, 2, row);
    }
}
