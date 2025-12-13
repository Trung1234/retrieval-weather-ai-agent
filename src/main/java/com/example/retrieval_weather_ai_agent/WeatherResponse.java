package com.example.retrieval_weather_ai_agent;

// 2. Định nghĩa Output (Dữ liệu trả về cho AI đọc)
public record WeatherResponse(double temp, int humidity, String description) {}
