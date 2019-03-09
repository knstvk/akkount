package akkount.web.operation;

import akkount.entity.Operation;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.math.BigDecimal;

@UiController("transfer-frame")
@UiDescriptor("transfer-frame.xml")
public class TransferFrame extends ScreenFragment implements OperationFrame {

    @Inject
    private InstanceContainer<Operation> operationDc;

    @Inject
    private TextField<String> amount1Field;
    @Inject
    private TextField<String> amount2Field;

    @Inject
    private Label<String> currency1Lab;

    @Inject
    private Label<String> currency2Lab;

    @Inject
    private AmountCalculator amountCalculator;

    @Override
    public void postInit(Operation item) {
        amountCalculator.initAmount(amount1Field, item.getAmount1());
        amountCalculator.initAmount(amount2Field, item.getAmount2());

        amount1Field.addValueChangeListener(e -> {
            if (e.getValue() != null && StringUtils.isBlank(amount2Field.getValue())) {
                amount2Field.setValue(e.getValue());
            }
        });

        setCurrency1Label(item);
        setCurrency2Label(item);

        operationDc.addItemPropertyChangeListener(e -> {
            switch (e.getProperty()) {
                case "acc1":
                    setCurrency1Label(e.getItem());
                    break;
                case "acc2":
                    setCurrency2Label(e.getItem());
                    break;
            }
        });
    }

    @Override
    public void postValidate(ValidationErrors errors) {
        BigDecimal value1 = amountCalculator.calculateAmount(amount1Field, errors);
        if (value1 != null)
            operationDc.getItem().setAmount1(value1);

        BigDecimal value2 = amountCalculator.calculateAmount(amount2Field, errors);
        if (value2 != null)
            operationDc.getItem().setAmount2(value2);
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