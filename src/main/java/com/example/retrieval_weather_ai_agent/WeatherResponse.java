package com.example.retrieval_weather_ai_agent;

/***
 * Định nghĩa Output (Dữ liệu trả về cho AI đọc)
 * @param temp
 * @param humidity
 * @param description
 */
public record WeatherResponse(double temp, int humidity, String description) {}
