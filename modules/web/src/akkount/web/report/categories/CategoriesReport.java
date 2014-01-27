package akkount.web.report.categories;

import akkount.entity.CategoryAmount;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import com.haulmont.cuba.gui.components.Component;

public class CategoriesReport extends AbstractWindow {

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

    @Override
    public void init(Map<String, Object> params) {
    }

    public void apply1(Component source) {
        Map<String, Object> params = new HashMap<>();
        params.put("from", from1.getValue());
        params.put("to", to1.getValue());
        ds1.refresh(params);
    }

    public void apply2(Component source) {
        Map<String, Object> params = new HashMap<>();
        params.put("from", from2.getValue());
        params.put("to", to2.getValue());
        ds2.refresh(params);
    }
}