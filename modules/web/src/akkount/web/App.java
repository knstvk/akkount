package akkount.web;

import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.AppWindow;
import com.haulmont.cuba.web.DefaultApp;

public class App extends DefaultApp {

    @Override
    protected AppWindow createAppWindow(AppUI ui) {
        return new AkkAppWindow(ui);
    }
}