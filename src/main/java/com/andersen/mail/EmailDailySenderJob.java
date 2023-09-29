package com.andersen.mail;

import com.andersen.report.ReportService;
import com.andersen.report.ReportServiceImpl;
import com.andersen.util.PropertiesLoader;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDate;

public class EmailDailySenderJob implements Job {

    private static ReportService reportService = ReportServiceImpl.getInstance();
    private static final PropertiesLoader props = new PropertiesLoader();
    private final String from = props.getProperty("mail.from");
    private final String password = props.getProperty("mail.password");
    private final String host = props.getProperty("mail.host");
    private final int port = Integer.parseInt(props.getProperty("mail.port"));
    private final String to = props.getProperty("mail.to");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("\n\n\n------sending email-----------\n\n\n");
        String subject = "Daily report";
        String body = "It's a today daily report: " + LocalDate.now();
        String reportFile = props.getProperty("reportPath");
        String report = reportService.createDailyReport(LocalDate.now(), reportFile);
        MailWithAttachmentService mail = new MailWithAttachmentService(from, password, host, port, to);
        mail.sendMail(subject, body, report);
    }
}
