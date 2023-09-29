package com.andersen.telegram;

import com.andersen.util.PropertiesLoader;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBot extends TelegramLongPollingBot {


    PropertiesLoader props = new PropertiesLoader();
    private final String username = props.getProperty("bot.name");
    private final String token = props.getProperty("bot.token");



    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }
}
