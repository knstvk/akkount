/*
 * Copyright (c) 2015 akkount
 */

package akkount.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;

@Source(type = SourceType.DATABASE)
public interface AkkConfig extends Config {

    @Property("akk.slackToken")
    String getSlackToken();

    @Property("akk.slackVerificationToken")
    String getSlackVerificationToken();

    @Property("akk.slackUserLogin")
    @Default("slack")
    String getSlackUserLogin();

    @Property("akk.defaultAccount")
    @Default("cash")
    String getDefaultAccount();

    @Property("akk.defaultCategory")
    @Default("food")
    String getDefaultCategory();
}
