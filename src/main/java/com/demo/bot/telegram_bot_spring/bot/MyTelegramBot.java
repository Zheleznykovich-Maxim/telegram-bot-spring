package com.demo.bot.telegram_bot_spring.bot;

import com.demo.bot.telegram_bot_spring.config.BotConfigProperties;
import com.demo.bot.telegram_bot_spring.service.MessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    private final BotConfigProperties config;
    private final MessageService messageService;

    public MyTelegramBot(BotConfigProperties config, MessageService messageService) {
        this.config = config;
        this.messageService = messageService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();

            String response;
            try {
                if ("/reset".equalsIgnoreCase(text.trim())) {
                    messageService.resetHistory(chatId);
                    response = "История очищена.";
                } else {
                    response = messageService.handleMessage(chatId, text);
                }

                SendMessage message = new SendMessage(chatId, response);
                execute(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
