package akkount.service;

import akkount.config.AkkConfig;
import akkount.entity.Account;
import akkount.entity.Category;
import akkount.entity.Operation;
import akkount.entity.OperationType;
import com.google.common.base.Splitter;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.View;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

        return createExpense(acc, amount, cat);
    }

    private String createExpense(String acc, BigDecimal amount, String cat) {
        Optional<Account> optionalAccount = dataManager.load(Account.class)
                .query("select a from akk$Account a where lower(a.name) = :name")
                .parameter("name", acc.toLowerCase())
                .view(View.MINIMAL)
                .optional();
        if (!optionalAccount.isPresent())
            return String.format("Account %s not found", acc);
        Account account = optionalAccount.get();

        Optional<Category> optionalCategory = dataManager.load(Category.class)
                .query("select c from akk$Category c where lower(c.name) = :name")
                .parameter("name", cat.toLowerCase())
                .view(View.MINIMAL)
                .optional();
        if (!optionalCategory.isPresent())
            return String.format("Category %s not found", cat);
        Category category = optionalCategory.get();

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