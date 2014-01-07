/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.web;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Formatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DecimalFormatter implements Formatter<BigDecimal> {

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);

    @Override
    public String format(BigDecimal value) {
        if (BigDecimal.ZERO.compareTo(value) == 0)
            return "";
        FormatStrings formatStrings = Datatypes.getFormatStrings(userSessionSource.getLocale());
        if (formatStrings == null)
            throw new IllegalStateException("FormatStrings are not defined for " + userSessionSource.getLocale());
        DecimalFormat format = new DecimalFormat("#,###", formatStrings.getFormatSymbols());
        return format.format(value);
    }
}
