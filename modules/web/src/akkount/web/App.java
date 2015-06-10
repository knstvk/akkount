package akkount.web;

import com.haulmont.cuba.web.DefaultApp;

public class App extends DefaultApp {

    public MainWindow getMainWindow() {
        return (akkount.web.MainWindow) getAppWindow().getMainWindow();
    }
}