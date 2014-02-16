package akkount.web.operation;

import akkount.entity.Operation;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.math.BigDecimal;

public class TransferFrame extends AbstractFrame implements OperationFrame {

    @Inject
    protected Datasource<Operation> operationDs;

    @Inject
    protected Label currency1Lab;

    @Inject
    protected Label currency2Lab;

    @Override
    public void postInit(Operation item) {
        setCurrency1Label(item);
        setCurrency2Label(item);

        operationDs.addListener(new DsListenerAdapter<Operation>() {
            @Override
            public void valueChanged(Operation source, String property, @Nullable Object prevValue, @Nullable Object value) {
                switch (property) {
                    case "acc1":
                        setCurrency1Label(source);
                        break;
                    case "acc2":
                        setCurrency2Label(source);
                        break;
                    case "amount1":
                        if (value != null
                                && (source.getAmount2() == null || source.getAmount2().equals(BigDecimal.ZERO))) {
                            source.setAmount2((BigDecimal) value);
                        }
                        break;
                }
            }
        });
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