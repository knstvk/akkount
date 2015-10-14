package akkount.web.operation;

import akkount.entity.Operation;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.ValidationErrors;

public interface OperationFrame extends Frame {

    void postInit(Operation item);

    void postValidate(ValidationErrors errors);
}
