package akkount.web.account;

import java.util.Map;
import com.haulmont.cuba.gui.components.AbstractEditor;
import akkount.entity.Account;
import com.haulmont.cuba.gui.components.LookupPickerField;

import javax.inject.Named;

public class AccountEdit extends AbstractEditor<Account> {

    @Named("fieldGroup.currency")
    private LookupPickerField currencyField;

    @Override
    public void init(Map<String, Object> params) {
        currencyField.addLookupAction();
    }
}