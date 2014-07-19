package akkount.web.operation;

import akkount.entity.Operation;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.math.BigDecimal;

public class ExpenseFrame extends AbstractFrame implements OperationFrame {

    @Inject
    private Datasource<Operation> operationDs;

    @Inject
    private TextField amountField;

    @Inject
    private Label currencyLab;

    @Inject
    private AmountCalculator amountCalculator;

    @Override
    public void postInit(Operation item) {
        amountCalculator.initAmount(amountField, item.getAmount1());

        setCurrencyLabel(item);

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
    public void postValidate(ValidationErrors errors) {
        BigDecimal value = amountCalculator.calculateAmount(amountField, errors);
        if (value != null)
            operationDs.getItem().setAmount1(value);

        operationDs.getItem().setAmount2(BigDecimal.ZERO);
    }

    private void setCurrencyLabel(Operation operation) {
        String currency = operation.getAcc1() != null ? operation.getAcc1().getCurrencyCode() : "";
        currencyLab.setValue(currency);
    }
}