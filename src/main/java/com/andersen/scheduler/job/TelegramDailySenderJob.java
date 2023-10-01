package com.andersen.scheduler.job;

import com.andersen.report.ReportService;
import com.andersen.report.ReportServiceImpl;
import com.andersen.telegram.TelegramBot;
import com.andersen.telegram.dao.ChatIDDao;
import com.andersen.telegram.dao.ChatIDDaoImpl;
import com.andersen.util.PropertiesLoader;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class TelegramDailySenderJob implements Job {

    private final PropertiesLoader props = new PropertiesLoader();
    private static ReportService reportService = ReportServiceImpl.getInstance();
    private final String reportPath = props.getProperty("report.path");
    private final TelegramBot telegramBot = TelegramBot.getInstance();

    private final ChatIDDao chatDao = ChatIDDaoImpl.getInstance();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Long> chatIDs = chatDao.getAll();
        SendDocument sendingMessage = new SendDocument();
        sendingMessage.setCaption("Blue team report");
        File report = reportService.createDailyReport(LocalDate.now(), reportPath);
        sendingMessage.setDocument(new InputFile(report));
        for (Long chatId : chatIDs) {
            sendingMessage.setChatId(chatId);
            telegramBot.sendMessage(sendingMessage);
        }
    }
}
