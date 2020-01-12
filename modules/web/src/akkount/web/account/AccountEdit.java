package akkount.web.account;

import akkount.entity.Account;
import com.haulmont.cuba.gui.screen.*;

@UiController("akk_Account.edit")
@UiDescriptor("account-edit.xml")
@EditedEntityContainer("accountDc")
@LoadDataBeforeShow
public class AccountEdit extends StandardEditor<Account> {
}