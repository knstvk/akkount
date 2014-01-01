/*
 * Copyright (c) 2013 knstvk.akkount
 */

package akkount.web;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.app.folders.FoldersPane;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LeftPanel extends FoldersPane {

    protected Messages messages = AppBeans.get(Messages.class);

    public LeftPanel(MenuBar menuBar, AppWindow appWindow) {
        super(menuBar, appWindow);
    }

    @Override
    public void init(Component parent) {
        Label label = new Label(messages.getMessage(getClass(), "LeftPanel.caption"));
        label.setStyleName("cuba-folders-pane-caption");
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.addComponent(label);
        addComponent(layout);
        super.init(parent);
    }
}
