package akkount.web.currency;

import akkount.entity.Currency;
import com.haulmont.cuba.gui.screen.*;

@UiController("akk$Currency.lookup")
@UiDescriptor("currency-browse.xml")
@LookupComponent("currencyTable")
@LoadDataBeforeShow
public class CurrencyBrowse extends StandardLookup<Currency> {
}