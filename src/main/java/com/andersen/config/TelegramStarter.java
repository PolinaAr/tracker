package com.andersen.config;

import com.andersen.exception.InitializeException;
import com.andersen.telegram.TelegramBot;
import com.andersen.telegram.TelegramBotConfiguration;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class TelegramStarter implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            TelegramBot telegramBot = new TelegramBot();
            TelegramBotConfiguration.telegramBotsApi(telegramBot);
        } catch (TelegramApiException e) {
            throw new InitializeException(e.getMessage());
        }
    }
}
