package akkount.service;

import akkount.entity.CategoryAmount;

import java.lang.String;
import java.util.Date;
import java.util.List;

public interface ReportService {
    String NAME = "akk_ReportService";

    List<CategoryAmount> getTurnoverByCategories(Date fromDate, Date toDate);
}