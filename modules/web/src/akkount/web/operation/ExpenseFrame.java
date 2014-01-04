package akkount.web.operation;

import java.util.Map;

import akkount.entity.Operation;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

public class ExpenseFrame extends AbstractFrame implements OperationFrame {

    @Inject
    protected Datasource<Operation> operationDs;

    @Inject
    protected Label currencyLab;

    @Override
    public void init(Map<String, Object> params) {
        operationDs.addListener(new DsListenerAdapter<Operation>() {
            @Override
            public void valueChanged(Operation source, String property, @Nullable Object prevValue, @Nullable Object value) {
                if ("acc1".equals(property)) {
                    setCurrencyLabel(source);
                }
            }
        });
    }

    @Override
    public void initItem(Operation item) {
        setCurrencyLabel(item);
    }

    private void setCurrencyLabel(Operation operation) {
        String currency = operation.getAcc1() != null ? operation.getAcc1().getCurrencyCode() : "";
        currencyLab.setValue(currency);
    }
}