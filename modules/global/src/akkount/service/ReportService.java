package akkount.service;

import akkount.entity.Category;
import akkount.entity.CategoryAmount;
import akkount.entity.CategoryType;
import akkount.entity.Currency;

import java.lang.String;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ReportService {
    String NAME = "akk_ReportService";

    List<CategoryAmount> getTurnoverByCategories(Date fromDate, Date toDate,
                                                 CategoryType categoryType, String currencyCode,
                                                 List<UUID> excludedCategories);
}