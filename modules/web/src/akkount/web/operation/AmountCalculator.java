/*
 * Copyright (c) 2014 akkount
 */

package akkount.web.operation;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import org.apache.commons.lang.StringUtils;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean
public class AmountCalculator {

    private static final Pattern EXPR_PATTERN = Pattern.compile(
            "([-+]?[0-9]*\\.?[0-9]+[\\-\\+\\*/])+([-+]?[0-9]*\\.?[0-9]+)");

    @Inject
    private Scripting scripting;

    @Inject
    private UserSessionSource userSessionSource;

    private Datatype<BigDecimal> decimalDatatype = Datatypes.getNN(BigDecimal.class);

    public void initAmount(TextField amountField, BigDecimal value) {
        com.vaadin.ui.TextField vTextField = WebComponentsHelper.unwrap(amountField);
        new CalcExtension(vTextField);

        amountField.setValue(decimalDatatype.format(value, userSessionSource.getUserSession().getLocale()));
    }

    @Nullable
    public BigDecimal calculateAmount(TextField amountField, ValidationErrors errors) {
        String text = amountField.getValue();
        if (StringUtils.isBlank(text))
            return null;

        Matcher matcher = EXPR_PATTERN.matcher(text);
        if (matcher.matches()) {
            Number number = scripting.evaluateGroovy(text, new HashMap<String, Object>());
            return BigDecimal.valueOf(number.doubleValue());
        } else {
            try {
                return Datatypes.getNN(BigDecimal.class).parse(text, userSessionSource.getUserSession().getLocale());
            } catch (ParseException e) {
                errors.add(amountField, e.getMessage());
                return null;
            }
        }
    }
}
