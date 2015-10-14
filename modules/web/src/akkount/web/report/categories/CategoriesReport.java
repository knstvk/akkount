package akkount.web.report.categories;

import akkount.entity.*;
import akkount.entity.Currency;
import akkount.service.UserDataKeys;
import akkount.service.UserDataService;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.time.DateUtils;

public class CategoriesReport extends AbstractWindow {

    @Inject
    protected OptionsGroup categoryTypeGroup;
    @Inject
    protected LookupField currencyField;
    @Inject
    protected LookupField periodTypeField;
    @Inject
    protected DateField from1;
    @Inject
    protected DateField from2;
    @Inject
    protected DateField to1;
    @Inject
    protected DateField to2;
    @Inject
    protected Table table1;
    @Inject
    protected Table table2;
    @Inject
    protected TextField totalField1;
    @Inject
    protected TextField totalField2;
    @Inject
    protected BoxLayout excludedBox;
    @Inject
    protected CollectionDatasource<CategoryAmount, UUID> ds1;
    @Inject
    protected CollectionDatasource<CategoryAmount, UUID> ds2;
    @Inject
    protected CollectionDatasource<Currency, UUID> currenciesDs;
    @Inject
    protected ComponentsFactory componentsFactory;
    @Inject
    protected UserDataService userDataService;

    private boolean doNotRefresh;

    private Map<Category, Component> excludedCategories = new HashMap<>();

    @Override
    public void init(Map<String, Object> params) {
        initCurrencies();
        initCategoryTypes();
        initPeriodTypes();
        initDates();
        initExcludedCategories();
        initShowOperationsActions();

        refreshDs1();
        refreshDs2();
    }

    private void initCurrencies() {
        currenciesDs.refresh();
        Currency currency = userDataService.loadEntity(UserDataKeys.CAT_REP_CURRENCY, Currency.class);
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
                userDataService.saveEntity(UserDataKeys.CAT_REP_CURRENCY, (Currency) value);
            }
        });
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

    private void initPeriodTypes() {
        Map<String, Object> options = new LinkedHashMap<>();
        options.put(getMessage("1month"), 1);
        options.put(getMessage("2months"), 2);
        options.put(getMessage("3months"), 3);
        options.put(getMessage("6months"), 6);
        options.put(getMessage("12months"), 12);

        periodTypeField.setOptionsMap(options);
        periodTypeField.setValue(1);

        periodTypeField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                Integer months = (Integer) value;
                if (months != null) {
                    doNotRefresh = true;
                    try {
                        Date end = to2.getValue();
                        from2.setValue(DateUtils.addMonths(end, -1 * months));
                        to1.setValue(DateUtils.addMonths(end, -1 * months));
                        from1.setValue(DateUtils.addMonths(end, -2 * months));
                    } finally {
                        doNotRefresh = false;
                    }
                    refreshDs1();
                    refreshDs2();
                }
            }
        });
    }

    private void initDates() {
        Date now = new Date();

        from1.setValue(DateUtils.addMonths(now, -2));
        to1.setValue(DateUtils.addMonths(now, -1));

        from2.setValue(DateUtils.addMonths(now, -1));
        to2.setValue(now);

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

    private void initExcludedCategories() {
        doNotRefresh = true;
        try {
            List<Category> categoryList = userDataService.loadEntityList(UserDataKeys.CAT_REP_EXCLUDED_CATEGORIES, Category.class);
            for (Category category : categoryList) {
                excludeCategory(category);
            }
        } finally {
            doNotRefresh = false;
        }

        table1.addAction(new ExcludeCategoryAction(table1));
        table2.addAction(new ExcludeCategoryAction(table2));
    }

    private void initShowOperationsActions() {
        ShowOperationsAction action1 = new ShowOperationsAction(table1, from1, to1);
        table1.addAction(action1);
        table1.setItemClickAction(action1);

        ShowOperationsAction action2 = new ShowOperationsAction(table2, from2, to2);
        table2.addAction(action2);
        table2.setItemClickAction(action2);
    }

    private void refreshDs1() {
        if (doNotRefresh)
            return;

        ds1.refresh(createDatasourceParams(from1.getValue(), to1.getValue()));

        totalField1.setEditable(true);
        totalField1.setValue(getTotalAmount(ds1));
        totalField1.setEditable(false);
    }

    private void refreshDs2() {
        if (doNotRefresh)
            return;

        ds2.refresh(createDatasourceParams(from2.getValue(), to2.getValue()));

        totalField2.setEditable(true);
        totalField2.setValue(getTotalAmount(ds2));
        totalField2.setEditable(false);
    }

    private Map<String, Object> createDatasourceParams(Object from, Object to) {
        Map<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);
        params.put("categoryType", categoryTypeGroup.getValue());
        params.put("currency", currencyField.getValue());
        params.put("excludedCategories", excludedCategories.keySet());
        return params;
    }

    private BigDecimal getTotalAmount(CollectionDatasource<CategoryAmount, UUID> datasource) {
        BigDecimal total = BigDecimal.ZERO;
        for (CategoryAmount ca : datasource.getItems()) {
            total = total.add(ca.getAmount());
        }
        return total;
    }

    private void excludeCategory(Category category) {
        if (excludedCategories.containsKey(category))
            return;

        BoxLayout box = componentsFactory.createComponent(HBoxLayout.class);
        box.setMargin(false, true, false, false);

        Label label = componentsFactory.createComponent(Label.class);
        label.setValue(category.getName());
        label.setAlignment(Alignment.MIDDLE_LEFT);
        box.add(label);

        LinkButton button = componentsFactory.createComponent(LinkButton.class);
        button.setIcon("icons/remove.png");
        button.setAction(new AbstractAction("") {
            @Override
            public void actionPerform(Component component) {
                for (Iterator<Map.Entry<Category, Component>> it = excludedCategories.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Category, Component> entry = it.next();
                    if (entry.getValue() == box) {
                        excludedBox.remove(box);
                        it.remove();
                        userDataService.removeEntity(UserDataKeys.CAT_REP_EXCLUDED_CATEGORIES, entry.getKey());
                        break;
                    }
                }
                refreshDs1();
                refreshDs2();
            }
        });
        box.add(button);

        excludedBox.add(box);
        excludedCategories.put(category, box);
        refreshDs1();
        refreshDs2();
    }

    private class ExcludeCategoryAction extends ItemTrackingAction {

        private Table table;

        public ExcludeCategoryAction(Table table) {
            super(table, "excludeCategory");
            this.table = table;
        }

        @Override
        public void actionPerform(Component component) {
            CategoryAmount categoryAmount = (CategoryAmount) table.getDatasource().getItem();
            if (categoryAmount != null) {
                excludeCategory(categoryAmount.getCategory());
                userDataService.addEntity(UserDataKeys.CAT_REP_EXCLUDED_CATEGORIES, categoryAmount.getCategory());
            }
        }
    }

    private class ShowOperationsAction extends ItemTrackingAction {

        private Table table;
        private DateField from;
        private DateField to;

        public ShowOperationsAction(Table table, DateField from, DateField to) {
            super(table, "showOperations");
            this.table = table;
            this.from = from;
            this.to = to;
        }

        @Override
        public void actionPerform(Component component) {
            CategoryAmount categoryAmount = (CategoryAmount) table.getDatasource().getItem();
            if (categoryAmount != null) {
                Map<String, Object> params = new HashMap<>();
                params.put("category", categoryAmount.getCategory());
                params.put("fromDate", from.getValue());
                params.put("toDate", to.getValue());
                if (categoryAmount.getCategory().getCatType() == CategoryType.EXPENSE) {
                    params.put("currency1", ((Currency) currencyField.getValue()).getCode());
                } else {
                    params.put("currency2", ((Currency) currencyField.getValue()).getCode());
                }
                openWindow("show-operations", WindowManager.OpenType.NEW_TAB, params);
            }
        }
    }

}