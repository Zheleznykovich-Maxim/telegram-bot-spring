package com.demo.bot.telegram_bot_spring.chat;

import com.demo.bot.telegram_bot_spring.config.ChatProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class GroqClient
{
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    private final ChatProperties chatProperties;

    public GroqClient(ChatProperties chatProperties) {
        this.chatProperties = chatProperties;
    }

    public String ask(List<Map<String, String>> prompt) throws IOException {
        String model = "llama3-70b-8192";

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", prompt
        );

        RequestBody requestBody = RequestBody.create(
                mapper.writeValueAsString(body),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(chatProperties.getAPI_URl())
                .addHeader("Authorization", "Bearer " + chatProperties.getAPI_KEY())
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();
            Map<?, ?> map = mapper.readValue(json, Map.class);
            Map<?, ?> message = (Map<?, ?>) ((Map<?, ?>) ((List<?>) map.get("choices")).get(0)).get("message");
            return message.get("content").toString().trim();
        }
    }

}
