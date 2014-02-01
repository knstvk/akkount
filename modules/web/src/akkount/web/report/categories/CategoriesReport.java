package akkount.web.report.categories;

import akkount.entity.*;
import akkount.entity.Currency;
import akkount.service.UserDataService;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

import com.haulmont.cuba.gui.data.ValueListener;
import org.apache.commons.lang.time.DateUtils;

public class CategoriesReport extends AbstractWindow {

    public static final String CURRENCY_KEY = "CategoriesReport.currency";
    @Inject
    protected OptionsGroup categoryTypeGroup;
    @Inject
    protected LookupField currencyField;
    @Inject
    protected DateField from1;
    @Inject
    protected DateField from2;
    @Inject
    protected DateField to1;
    @Inject
    protected DateField to2;
    @Inject
    protected CollectionDatasource<CategoryAmount, UUID> ds1;
    @Inject
    protected CollectionDatasource<CategoryAmount, UUID> ds2;
    @Inject
    protected CollectionDatasource<Currency, UUID> currenciesDs;
    @Inject
    protected UserDataService userDataService;

    @Override
    public void init(Map<String, Object> params) {
        initCategoryTypes();
        initCurrencies();
        initDates();
    }

    private void initCategoryTypes() {
        List<CategoryType> categoryTypes = new ArrayList<>();
        categoryTypes.add(CategoryType.EXPENSE);
        categoryTypes.add(CategoryType.INCOME);
        categoryTypeGroup.setOptionsList(categoryTypes);
        categoryTypeGroup.setValue(CategoryType.EXPENSE);

        categoryTypeGroup.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                refreshDs1();
                refreshDs2();
            }
        });
    }

    private void initCurrencies() {
        currenciesDs.refresh();
        Currency currency = userDataService.loadEntity(CURRENCY_KEY, Currency.class);
        if (currency == null) {
            Collection<Currency> currencies = currenciesDs.getItems();
            if (!currencies.isEmpty())
                currency = currencies.iterator().next();
        }
        currencyField.setValue(currency);

        currencyField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                refreshDs1();
                refreshDs2();
                userDataService.saveEntity(CURRENCY_KEY, (Currency) value);
            }
        });
    }

    private void initDates() {
        Date now = new Date();
        from1.setValue(DateUtils.addMonths(now, -1));
        to1.setValue(now);
        refreshDs1();

        from2.setValue(DateUtils.addMonths(now, -2));
        to2.setValue(DateUtils.addMonths(now, -1));
        refreshDs2();

        ValueListener period1Listener = new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                refreshDs1();
            }
        };
        from1.addListener(period1Listener);
        to1.addListener(period1Listener);

        ValueListener period2Listener = new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                refreshDs2();
            }
        };
        from2.addListener(period2Listener);
        to2.addListener(period2Listener);
    }

    private void refreshDs1() {
        Map<String, Object> params = new HashMap<>();
        params.put("from", from1.getValue());
        params.put("to", to1.getValue());
        params.put("categoryType", categoryTypeGroup.getValue());
        params.put("currency", currencyField.getValue());
        ds1.refresh(params);
    }

    private void refreshDs2() {
        Map<String, Object> params = new HashMap<>();
        params.put("from", from2.getValue());
        params.put("to", to2.getValue());
        params.put("categoryType", categoryTypeGroup.getValue());
        params.put("currency", currencyField.getValue());
        ds2.refresh(params);
    }
}