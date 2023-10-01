package com.andersen.report;

import java.io.File;
import java.time.LocalDate;

public interface ReportService {

    File createDailyReport(LocalDate localDate, String filePath);
}
