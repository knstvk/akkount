package akkount.web.operation;

import java.util.Map;
import java.util.Set;

import akkount.web.App;
import akkount.web.LeftPanel;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.RemoveAction;

import javax.inject.Inject;
import javax.inject.Named;

public class OperationBrowse extends AbstractLookup {

    @Inject
    protected Table operationTable;

    @Override
    public void init(Map<String, Object> params) {
        operationTable.addAction(new RemoveAction(operationTable) {
            @Override
            protected void afterRemove(Set selected) {
                LeftPanel leftPanel = App.getLeftPanel();
                if (leftPanel != null)
                    leftPanel.refreshBalance();
            }
        });
    }
}