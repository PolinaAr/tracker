package com.andersen.mail;

import com.andersen.report.ReportService;
import com.andersen.report.ReportServiceImpl;
import com.andersen.util.PropertiesLoader;

import java.time.LocalDate;

public class EmailDailySender {

    public static void main(String[] args) {
        EmailDailySender sender = new EmailDailySender();
        sender.sendEmail();
    }

    private static ReportService reportService = ReportServiceImpl.getInstance();
    private static final PropertiesLoader props = new PropertiesLoader();
    private final String from = props.getProperty("mail.from");
    private final String password = props.getProperty("mail.password");
    private final String host = props.getProperty("mail.host");
    private final int port = Integer.parseInt(props.getProperty("mail.port"));
    private final String to = props.getProperty("mail.to");

    public void sendEmail() {
        String subject = "Daily report";
        String body = "It's a today daily report: " + LocalDate.now();
        String reportFile = "src\\main\\resources\\reports\\DailyReport.pdf";
        String report = reportService.createDailyReport(LocalDate.now(), reportFile);
        MailWithAttachmentService mail = new MailWithAttachmentService(from, password, host, port, to);
        mail.sendMail(subject, body, report);
    }
}
