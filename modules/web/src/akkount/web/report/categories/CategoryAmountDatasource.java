/*
 * Copyright (c) 2014 akkount
 */

package akkount.web.report.categories;

import akkount.entity.CategoryAmount;
import akkount.entity.CategoryType;
import akkount.entity.Currency;
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
                return;

            Currency currency = (Currency) params.get("currency");
            if (currency == null)
                return;

            CategoryType categoryType = (CategoryType) params.get("categoryType");
            if (categoryType == null)
                categoryType = CategoryType.EXPENSE;

            List<CategoryAmount> list = service.getTurnoverByCategories(fromDate, toDate, categoryType, currency.getCode());
            for (CategoryAmount categoryAmount : list) {
                data.put(categoryAmount.getId(), categoryAmount);
            }
        } catch (Exception e) {
            dataLoadError = e;
        }
    }
}
