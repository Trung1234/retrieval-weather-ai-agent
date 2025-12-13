package com.example.retrieval_weather_ai_agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentController {

    private final ChatClient chatClient;

    public AgentController(ChatClient.Builder builder) {
        // 1. Chỉ cấu hình System Prompt mặc định ở đây
        this.chatClient = builder
                .defaultSystem("Bạn là một trợ lý thời tiết hữu ích. Nếu người dùng hỏi về thời tiết, hãy sử dụng công cụ currentWeatherFunction.")
                .build();
    }

    @GetMapping("/weather-chat")
    public String chat(@RequestParam(value = "message", defaultValue = "Thời tiết Hà Nội?") String message) {

        // 1. Tạo Options
        var options = GoogleGenAiChatOptions.builder()
                .toolName("currentWeatherFunction") // Nếu vẫn đỏ, hãy thử .tools("currentWeatherFunction")
                .build();

        // 2. Tạo Prompt thủ công
        Prompt prompt = new Prompt(message, options);

        // 3. Gọi trực tiếp
        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}