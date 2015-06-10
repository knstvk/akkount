package akkount.web.operation;

import akkount.entity.Operation;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.ValidationErrors;

public interface OperationFrame extends IFrame {

    void postInit(Operation item);

    void postValidate(ValidationErrors errors);
}
