package com.andersen.telegram;

import com.andersen.telegram.dao.ChatIDDao;
import com.andersen.telegram.dao.ChatIDDaoImpl;
import com.andersen.util.PropertiesLoader;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {
    private PropertiesLoader props = new PropertiesLoader();
    private final String botName = props.getProperty("bot.name");
    private final String token = props.getProperty("bot.token");

    private final Long CHAT_ID_NOT_FOUND = -1L;

    private final ChatIDDao chatDao = ChatIDDaoImpl.getInstance();

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        if (update.getMessage().getText().equalsIgnoreCase("/start")){
            String username = update.getMessage().getFrom().getUserName();
            if (!isChatIdExists(chatId)) {
                addChatIdToDatabase(chatId);
                sendWelcomeMessage(username, chatId);
            }else{
                sendMessage(new SendMessage(chatId.toString(), "You already subscribed"));
            }
        }
        if (update.getMessage().getText().equalsIgnoreCase("/stop")){
            boolean deleted = chatDao.delete(chatId);
            System.out.println("deleted = " + deleted);
            sendGoodbyeMessage(chatId);
        }
    }

    private boolean isChatIdExists(Long chatId) {
        return chatDao.getById(chatId) != CHAT_ID_NOT_FOUND;
    }

    private void sendGoodbyeMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("You now unsubscribed from daily report"
                + "\n To start receiving reports type \'/start\'");
        sendMessage(message);
    }

    private void sendWelcomeMessage(String username, Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hello " + username + " you have submitted subscription to daily report"
        + "\n To stop receiving reports type \'/stop\'");
        sendMessage(message);
    }

    public void sendReport(){
        List<Long> chatIDs = chatDao.getAll();
        SendDocument sendingMessage = new SendDocument();
        sendingMessage.setCaption("Blue team report");
        sendingMessage.setDocument(new InputFile(new File(props.getProperty("pathToReportFile"))));
        for (Long chatId : chatIDs) {
            sendingMessage.setChatId(chatId);
            sendMessage(sendingMessage);
        }
    }

    private void sendMessage(SendDocument message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void addChatIdToDatabase(Long chatId){
        chatDao.create(chatId);
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}
