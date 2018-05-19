/*
 * Copyright (c) 2015 akkount
 */

package akkount.event;

import com.haulmont.addon.globalevents.GlobalApplicationEvent;
import com.haulmont.addon.globalevents.GlobalUiEvent;

public class BalanceChangedEvent extends GlobalApplicationEvent implements GlobalUiEvent {

    public BalanceChangedEvent(Object source) {
        super(source);
    }
}
