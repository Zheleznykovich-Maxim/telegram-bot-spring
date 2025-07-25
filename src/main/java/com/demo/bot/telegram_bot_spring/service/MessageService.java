package com.demo.bot.telegram_bot_spring.service;

import com.demo.bot.telegram_bot_spring.chat.GroqClient;
import com.demo.bot.telegram_bot_spring.model.UserMessage;
import com.demo.bot.telegram_bot_spring.repository.UserMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {
    private final UserMessageRepository repository;
    private final GroqClient groqClient;

    public MessageService(UserMessageRepository repository, GroqClient groqClient) {
        this.repository = repository;
        this.groqClient = groqClient;
    }

    @Transactional
    public String handleMessage(String chatId, String text) throws IOException {
        UserMessage message = new UserMessage();
        message.setChatId(chatId);
        message.setContent(text);
        message.setRole("user");
        repository.save(message);

        List<UserMessage> history = repository.findTop20ByChatIdOrderByTimestampAsc(chatId);

        List<Map<String, String>> messages = history.stream()
                .map(msg -> Map.of("role", msg.getRole(), "content", msg.getContent()))
                .toList();

        String assistantReply = groqClient.ask(messages);
        UserMessage assistMessage = new UserMessage();
        assistMessage.setChatId(chatId);
        assistMessage.setRole("assistant");
        assistMessage.setContent(assistantReply);

        repository.save(assistMessage);

        return assistantReply;
    }

    @Transactional
    public void resetHistory(String chatId) {
        repository.deleteByChatId(chatId);
    }
}
