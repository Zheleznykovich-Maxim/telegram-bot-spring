package com.demo.bot.telegram_bot_spring.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "chat.credentials")
@Getter
@Setter
public class ChatProperties {
    private String API_KEY;
    private String API_URl;
}
