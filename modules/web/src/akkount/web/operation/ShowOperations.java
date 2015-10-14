package akkount.web.operation;

import akkount.entity.Category;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateDatatype;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Date;
import java.util.Map;

public class ShowOperations extends AbstractWindow {

    @Inject
    private Label descriptionLab;
    @Inject
    private UserSession userSession;

    @Override
    public void init(Map<String, Object> params) {
        boolean isExpense = params.get("currency1") != null;
        Datatype dateDatatype = Datatypes.get(DateDatatype.NAME);
        descriptionLab.setValue(formatMessage("showOperationsDescription",
                isExpense ? getMessage("expenseTab") : getMessage("incomeTab"),
                ((Category) params.get("category")).getName(),
                dateDatatype.format(params.get("fromDate"), userSession.getLocale()),
                dateDatatype.format(params.get("toDate"), userSession.getLocale()),
                isExpense ? params.get("currency1") : params.get("currency2")
        ));
    }
}