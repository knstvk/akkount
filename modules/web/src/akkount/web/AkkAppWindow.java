/* 
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.web;

import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.app.folders.FoldersPane;

import javax.annotation.Nullable;

/**
 * @author krivopustov
 * @version $Id$
 */
public class AkkAppWindow extends AppWindow {

    public AkkAppWindow(AppUI ui) {
        super(ui);
    }

    @Nullable
    @Override
    protected FoldersPane createFoldersPane() {
        return new LeftPanel(menuBar, this);
    }
}
