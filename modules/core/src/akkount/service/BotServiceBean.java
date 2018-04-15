package akkount.service;

import akkount.config.AkkConfig;
import akkount.entity.Account;
import akkount.entity.Category;
import akkount.entity.Operation;
import akkount.entity.OperationType;
import com.google.common.base.Splitter;
import com.haulmont.cuba.core.global.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

@Service(BotService.NAME)
public class BotServiceBean implements BotService {

    @Inject
    private AkkConfig akkConfig;

    @Inject
    private DataManager dataManager;

    @Inject
    private Metadata metadata;

    @Inject
    private TimeSource timeSource;

    public String processMessage(String message) {
        List<String> parts = Splitter.on(' ').omitEmptyStrings().trimResults().splitToList(message);

        BigDecimal amount = null;
        String acc = akkConfig.getDefaultAccount();
        String cat = akkConfig.getDefaultCategory();

        for (int i = 0; i < parts.size(); i++) {
            try {
                String part = parts.get(i);
                amount = new BigDecimal(part);
                // no exception, then this part is amount
                int amountStart = message.indexOf(part);
                int amountEnd = amountStart + part.length();
                if (i > 0) {
                    acc = message.substring(0, amountStart).trim();
                }
                if (i < parts.size() - 1) {
                    cat = message.substring(amountEnd).trim();
                }
            } catch (NumberFormatException e) {
                // ignore and go to the next word
            }
        }
        if (amount == null) {
            return "Expected [account name] amount [category name]";
        }


//        if (parts.size() == 3) {
//            acc = parts.get(0);
//            amount = new BigDecimal(parts.get(1));
//            cat = parts.get(2);
//        } else if (parts.size() == 2) {
//            acc = akkConfig.getDefaultAccount();
//            amount = new BigDecimal(parts.get(0));
//            cat = parts.get(1);
//        } else {
//            return "Expected [acc amount category] or [amount category]";
//        }
        return createExpense(acc, amount, cat);
    }

    private String createExpense(String acc, BigDecimal amount, String cat) {
        LoadContext.Query accQuery = LoadContext.createQuery("select a from akk$Account a where lower(a.name) = :name")
                .setParameter("name", acc.toLowerCase());
        Account account = dataManager.load(LoadContext.create(Account.class).setQuery(accQuery).setView(View.MINIMAL));
        if (account == null)
            return String.format("Account %s not found", acc);

        LoadContext.Query catQuery = LoadContext.createQuery("select c from akk$Category c where lower(c.name) = :name")
                .setParameter("name", cat.toLowerCase());
        Category category = dataManager.load(LoadContext.create(Category.class).setQuery(catQuery).setView(View.MINIMAL));
        if (category == null)
            return String.format("Category %s not found", cat);

        Operation operation = metadata.create(Operation.class);
        operation.setOpDate(timeSource.currentTimestamp());
        operation.setOpType(OperationType.EXPENSE);
        operation.setAcc1(account);
        operation.setCategory(category);
        operation.setAmount1(amount);
        dataManager.commit(operation);
        return String.format("Spent %.2f from %s on %s", amount, account.getName(), category.getName());
    }

}