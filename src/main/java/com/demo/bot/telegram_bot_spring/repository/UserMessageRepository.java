package com.demo.bot.telegram_bot_spring.repository;

import com.demo.bot.telegram_bot_spring.model.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    List<UserMessage> findTop20ByChatIdOrderByTimestampAsc(String chatId);
    void deleteByChatId(String chatId);
}
