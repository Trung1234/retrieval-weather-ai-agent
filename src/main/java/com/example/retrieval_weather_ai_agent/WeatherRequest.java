package com.example.retrieval_weather_ai_agent;


import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * Định nghĩa Input cho function (AI sẽ điền vào đây)
 * @param location
 * @param unit
 */
@JsonClassDescription("Lấy thông tin thời tiết cho một địa điểm cụ thể")
public record WeatherRequest(
        @JsonPropertyDescription("Tên thành phố, ví dụ: Hanoi, London")
        String location,

        @JsonPropertyDescription("Đơn vị đo: metric (độ C) hoặc imperial (độ F)")
        String unit
) {}

