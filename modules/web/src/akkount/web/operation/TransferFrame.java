package akkount.web.operation;

import java.util.Map;

import akkount.entity.Operation;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class TransferFrame extends AbstractFrame implements OperationFrame {

    @Inject
    protected Datasource<Operation> operationDs;

    @Inject
    protected Label currency1Lab;

    @Inject
    protected Label currency2Lab;

    @Override
    public void init(Map<String, Object> params) {
        operationDs.addListener(new DsListenerAdapter<Operation>() {
            @Override
            public void valueChanged(Operation source, String property, @Nullable Object prevValue, @Nullable Object value) {
                if ("acc1".equals(property)) {
                    setCurrency1Label(source);
                } else if ("acc2".equals(property)) {
                    setCurrency2Label(source);
                }
            }
        });
    }

    @Override
    public void initItem(Operation item) {
        setCurrency1Label(item);
        setCurrency2Label(item);
    }

    private void setCurrency2Label(Operation operation) {
        String currency = operation.getAcc2() != null ? operation.getAcc2().getCurrencyCode() : "";
        currency2Lab.setValue(currency);
    }

    private void setCurrency1Label(Operation operation) {
        String currency = operation.getAcc1() != null ? operation.getAcc1().getCurrencyCode() : "";
        currency1Lab.setValue(currency);
    }
}