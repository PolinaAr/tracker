package com.andersen.scheduler.job;

import com.andersen.mail.MailWithAttachmentService;
import com.andersen.report.ReportService;
import com.andersen.report.ReportServiceImpl;
import com.andersen.util.PropertiesLoader;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;
import java.time.LocalDate;

public class EmailDailySenderJob implements Job {

    private static ReportService reportService = ReportServiceImpl.getInstance();
    private static final PropertiesLoader props = new PropertiesLoader();
    private final String from = props.getProperty("mail.from");
    private final String password = props.getProperty("mail.password");
    private final String host = props.getProperty("mail.host");
    private final int port = Integer.parseInt(props.getProperty("mail.port"));
    private final String to = props.getProperty("mail.to");
    private final String reportFile = props.getProperty("report.path");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String subject = "Daily report";
        String body = "It's a today daily report: " + LocalDate.now();
        File report = reportService.createDailyReport(LocalDate.now(), reportFile);
        MailWithAttachmentService mail = new MailWithAttachmentService(from, password, host, port, to);
        mail.sendMail(subject, body, report);
    }
}
