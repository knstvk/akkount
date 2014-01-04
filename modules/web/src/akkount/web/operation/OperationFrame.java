/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.web.operation;

import akkount.entity.Operation;
import com.haulmont.cuba.gui.components.IFrame;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface OperationFrame extends IFrame {

    void initItem(Operation item);
}
