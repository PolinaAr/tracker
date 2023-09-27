package com.andersen.report;

import java.time.LocalDate;

public interface ReportService {

    void createDailyReport(LocalDate localDate);
}
