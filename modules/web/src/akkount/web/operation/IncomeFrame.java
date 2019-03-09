package akkount.web.operation;

import akkount.entity.Operation;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.math.BigDecimal;

@UiController("income-frame")
@UiDescriptor("income-frame.xml")
public class IncomeFrame extends ScreenFragment implements OperationFrame {

    @Inject
    private InstanceContainer<Operation> operationDc;

    @Inject
    private TextField<String> amountField;

    @Inject
    private Label<String> currencyLab;

    @Inject
    private AmountCalculator amountCalculator;

    @Override
    public void postInit(Operation item) {
        getScreenData().loadAll();

        amountCalculator.initAmount(amountField, item.getAmount2());

        setCurrencyLabel(item);

        operationDc.addItemPropertyChangeListener(e -> {
            if ("acc2".equals(e.getProperty())) {
                setCurrencyLabel(e.getItem());
            }
        });
    }

    @Override
    public void postValidate(ValidationErrors errors) {
        BigDecimal value = amountCalculator.calculateAmount(amountField, errors);
        if (value != null)
            operationDc.getItem().setAmount2(value);

        operationDc.getItem().setAmount1(BigDecimal.ZERO);
    }

    private void setCurrencyLabel(Operation operation) {
        String currency = operation.getAcc2() != null ? operation.getAcc2().getCurrencyCode() : "";
        currencyLab.setValue(currency);
    }
}