package com.andersen.report;

import java.time.LocalDate;

public interface ReportService {

    String createDailyReport(LocalDate localDate, String reportName);
}
