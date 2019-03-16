package akkount.web.operation;

import com.haulmont.cuba.web.widgets.WebJarResource;
import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.TextField;

@JavaScript("textfieldcalc.js")
@WebJarResource("jquery:jquery.min.js")
public class CalcExtension extends AbstractJavaScriptExtension {

    public CalcExtension(TextField textField) {
        super.extend(textField);
    }
}
