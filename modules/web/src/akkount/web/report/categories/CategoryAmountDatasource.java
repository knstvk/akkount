/*
 * Copyright (c) 2014 akkount
 */

package akkount.web.report.categories;

import akkount.entity.CategoryAmount;
import akkount.service.ReportService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CategoryAmountDatasource extends CollectionDatasourceImpl<CategoryAmount, UUID> {

    private ReportService service = AppBeans.get(ReportService.NAME);

    @Override
    protected void loadData(Map<String, Object> params) {
        data.clear();

        dataLoadError = null;
        try {
            Date fromDate = (Date) params.get("from");
            Date toDate = (Date) params.get("to");
            if (fromDate == null || toDate == null || toDate.compareTo(fromDate) < 0)
                throw new IllegalArgumentException("Invalid dates");

            List<CategoryAmount> list = service.getTurnoverByCategories(fromDate, toDate);
            for (CategoryAmount categoryAmount : list) {
                data.put(categoryAmount.getId(), categoryAmount);
            }
        } catch (Exception e) {
            dataLoadError = e;
        }
    }
}
