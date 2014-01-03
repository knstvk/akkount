package akkount.web;

import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.DefaultApp;

import javax.annotation.Nullable;

public class App extends DefaultApp {

    @Override
    protected AppWindow createAppWindow(AppUI ui) {
        return new AkkAppWindow(ui);
    }

    @Nullable
    public static LeftPanel getLeftPanel() {
        return (LeftPanel) AppUI.getCurrent().getAppWindow().getFoldersPane();
    }
}