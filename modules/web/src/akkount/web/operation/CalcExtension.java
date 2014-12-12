/*
 * Copyright (c) 2014 akkount
 */

package akkount.web.operation;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.TextField;

/**
 * @author krivopustov
 * @version $Id$
 */
@JavaScript("textfieldcalc.js")
public class CalcExtension extends AbstractJavaScriptExtension {

    public CalcExtension(TextField textField) {
        super.extend(textField);
    }
}
