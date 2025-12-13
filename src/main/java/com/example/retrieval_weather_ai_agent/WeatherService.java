package com.example.retrieval_weather_ai_agent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.function.Function;

@Service
public class WeatherService implements Function<WeatherRequest, WeatherResponse> {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final RestClient restClient = RestClient.create();

    @Override
    public WeatherResponse apply(WeatherRequest request) {
        System.out.println("Calling Weather API for: " + request.location());

        // Gọi OpenWeatherMap API
        var response = restClient.get()
                .uri(apiUrl + "?q={city}&appid={key}&units={unit}",
                        request.location(), apiKey, request.unit() == null ? "metric" : request.unit())
                .retrieve()
                .body(OpenWeatherMapResponse.class); // Cần tạo class này để map JSON

        if (response != null && response.main != null) {
            return new WeatherResponse(
                    response.main.temp,
                    response.main.humidity,
                    response.weather[0].description
            );
        }
        return new WeatherResponse(0, 0, "Không tìm thấy dữ liệu");
    }

    // Class nội bộ để map JSON từ OpenWeatherMap (cấu trúc đơn giản hóa)
    record OpenWeatherMapResponse(Main main, Weather[] weather) {}
    record Main(double temp, int humidity) {}
    record Weather(String description) {}
}