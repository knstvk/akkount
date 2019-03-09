package akkount.web.operation;

import akkount.entity.Operation;
import com.haulmont.cuba.gui.components.Fragment;
import com.haulmont.cuba.gui.components.ValidationErrors;

public interface OperationFrame {

    Fragment getFragment();

    void postInit(Operation item);

    void postValidate(ValidationErrors errors);
}
