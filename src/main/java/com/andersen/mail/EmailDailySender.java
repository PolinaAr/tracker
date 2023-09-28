package com.andersen.mail;

import com.andersen.report.ReportService;
import com.andersen.report.ReportServiceImpl;
import com.andersen.util.PropertiesLoader;

import java.time.LocalDate;

public class EmailDailySender {

    private static ReportService reportService = ReportServiceImpl.getInstance();
    private static final PropertiesLoader props = new PropertiesLoader();
    private final String from = props.getProperty("mail.from");
    private final String password = props.getProperty("mail.password");
    private final String host = props.getProperty("mail.host");
    private final int port = Integer.parseInt(props.getProperty("mail.port"));
    private final String to = props.getProperty("mail.to");

    public void sendEmail(){
        String subject = "Daily report";
        String body = "It's a today daily report: " + LocalDate.now();
        String reportName = reportService.createDailyReport(LocalDate.now(), "/src/main/resources/reports/report.pdf");
        MailWithAttachmentService mail = new MailWithAttachmentService(from, password, host, port, to);
        mail.sendMail(subject, body, reportName);
    }
}
