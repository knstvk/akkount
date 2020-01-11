package akkount.web.currency;

import akkount.entity.Currency;
import com.haulmont.cuba.gui.screen.*;

@UiController("akk_Currency.edit")
@UiDescriptor("currency-edit.xml")
@EditedEntityContainer("currencyDc")
@LoadDataBeforeShow
public class CurrencyEdit extends StandardEditor<Currency> {
}