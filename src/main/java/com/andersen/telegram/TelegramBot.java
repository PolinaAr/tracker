package com.andersen.telegram;

import com.andersen.telegram.dao.ChatIDDao;
import com.andersen.telegram.dao.ChatIDDaoImpl;
import com.andersen.util.PropertiesLoader;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {

    private static final String GOODBYE_MESSAGE = "You now unsubscribed from daily report"
            + "\nTo start receiving reports type \'/start\'";
    private static final String ALREADY_SUBSCRIBED = "You already subscribed";
    private static String HELLO_MESSAGE = "Hello %s you have submitted subscription to daily report"
            + "\n To stop receiving reports type \'/stop\'";

    private static TelegramBot telegramBot;
    private PropertiesLoader props = new PropertiesLoader();
    private final String botName = props.getProperty("bot.name");
    private final String token = props.getProperty("bot.token");

    public static TelegramBot getInstance() {
        if (telegramBot == null) {
            telegramBot = new TelegramBot();
        }
        return telegramBot;
    }

    private final Long CHAT_ID_NOT_FOUND = -1L;

    private final ChatIDDao chatDao = ChatIDDaoImpl.getInstance();

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        if (update.getMessage().getText().equalsIgnoreCase("/start")){
            String username = update.getMessage().getFrom().getUserName();
            if (!isChatIdExists(chatId)) {
                addChatIdToDatabase(chatId);
                sendMessage(new SendMessage(chatId.toString(),
                        String.format(HELLO_MESSAGE, username)));
            }else{
                sendMessage(new SendMessage(chatId.toString(),
                        ALREADY_SUBSCRIBED));
            }
        }
        if (update.getMessage().getText().equalsIgnoreCase("/stop")){
            chatDao.delete(chatId);
            sendMessage(new SendMessage(chatId.toString(), GOODBYE_MESSAGE));
        }
    }

    private boolean isChatIdExists(Long chatId) {
        return chatDao.getById(chatId) != CHAT_ID_NOT_FOUND;
    }

    public void sendMessage(SendDocument message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(SendMessage message){
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
