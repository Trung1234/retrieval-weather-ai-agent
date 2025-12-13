package com.example.retrieval_weather_ai_agent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import java.util.function.Function;

@Configuration
public class AIConfig {

    @Bean
    @Description("Lấy thông tin thời tiết hiện tại dựa trên địa điểm")
    public Function<WeatherRequest, WeatherResponse> currentWeatherFunction(WeatherService weatherService) {
        return weatherService;
    }
}