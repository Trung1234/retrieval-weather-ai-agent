## 🏗️ 1. Kiến trúc hệ thống (Architecture Flow)

Dưới đây là luồng xử lý request khi người dùng hỏi về thời tiết:
### Sequence Diagram
```mermaid
sequenceDiagram
    autonumber
    participant User as User (Client)
    participant Controller as AgentController
    participant SpringAI as Spring AI (ChatClient)
    participant Gemini as Google Gemini 2.5
    participant Tool as WeatherService
    participant OWM as OpenWeatherMap API

    User->>Controller: GET /weather-chat (message)
    Controller->>SpringAI: Prompt + Tool Definitions
    SpringAI->>Gemini: Gửi Request
    
    Note right of Gemini: AI phát hiện intent "thời tiết"<br/>-> Yêu cầu gọi Function
    
    Gemini-->>SpringAI: Tool Execution Request
    SpringAI->>Tool: Execute currentWeatherFunction()
    Tool->>OWM: Call External API
    OWM-->>Tool: Return JSON Data
    Tool-->>SpringAI: Return Function Result
    
    SpringAI->>Gemini: Gửi kết quả Tool cho AI
    Gemini-->>SpringAI: Sinh câu trả lời tự nhiên (Final Response)
    SpringAI-->>Controller: Return String
    Controller-->>User: Response Body
```
### Flowchart
```mermaid
flowchart TD
    Start([User gửi câu hỏi]) --> A[AgentController nhận Request]
    A --> B[Spring AI gửi Prompt tới Gemini]
    
    B --> C{Gemini phân tích:<br/>Cần dùng Tool không?}
    
    C -- Không --> D[Gemini tự trả lời dựa trên kiến thức có sẵn]
    
    C -- Có (Hỏi thời tiết) --> E[Spring AI kích hoạt WeatherService]
    E --> F[Gọi OpenWeatherMap API]
    F --> G[Nhận dữ liệu JSON thời tiết]
    G --> H[Gửi dữ liệu về lại Gemini]
    
    H --> I[Gemini tổng hợp thông tin + Câu hỏi gốc]
    D --> J[Tạo câu trả lời tự nhiên]
    I --> J
    
    J --> End([Trả về phản hồi cho User])

  
```
### Class Diagram
```mermaid
classDiagram
    direction LR

    %% 1. Components (Các Bean chính)
    class AgentController {
        +ChatClient chatClient
        +String chat(String message)
    }

    class WeatherService {
        +Function~WeatherRequest, WeatherResponse~ currentWeatherFunction()
        +WeatherResponse apply(WeatherRequest req)
    }

    class ChatClient {
        <<Interface>>
        +String call(...)
    }

    %% 2. Data Transfer Objects (DTOs/Records)
    class WeatherRequest {
        <<Record>>
        +String location
        +String unit
    }

    class WeatherResponse {
        <<Record>>
        +double temp
        +int humidity
        +String description
    }
    
    class OpenWeatherMapResponse {
        <<Record>>
        -Main main
        -Weather[] weather
    }

    class Main {
        <<Record>>
        +double temp
        +int humidity
    }
    
    class Weather {
        <<Record>>
        +String description
    }

    %% 3. Relationships (Mối quan hệ)
    AgentController --> ChatClient : sử dụng
    ChatClient ..> WeatherService : gọi (Tool Use)
    WeatherService ..> WeatherRequest : input
    WeatherService ..> WeatherResponse : output
    WeatherService --> OpenWeatherMapResponse : ánh xạ từ API ngoài
    OpenWeatherMapResponse *-- Main : chứa 1
    OpenWeatherMapResponse *-- Weather : chứa 1..*

  
```
## 🚀 2. Yêu cầu hệ thống (Prerequisites)

### 💻 Môi trường (Development Environment)
- [x] **Java:** JDK 17+
- [x] **Build Tool:** Maven / Gradle
- [x] **IDE:** IntelliJ IDEA (Recommended) / VS Code / Eclipse

### 🗝️ API Keys Required
Để chạy được Agent, bạn cần chuẩn bị 2 keys sau (sau đó thay vào file application.properties):

| Service | Mô tả | Đăng ký tại |
| :--- | :--- | :--- |
| **Google Gemini** | Dùng làm "bộ não" AI cho Agent | [Google AI Studio ↗](https://aistudio.google.com/) |
| **OpenWeatherMap** | Cung cấp dữ liệu thời tiết thực tế | [OpenWeatherMap ↗](https://openweathermap.org/) |

> **⚠️ Lưu ý:**
> * Đối với **OpenWeatherMap**: Sau khi tạo key mới, có thể mất từ 10-15 phút để key được kích hoạt.
> * Hãy bảo mật API Key của bạn, không commit trực tiếp lên Github (sử dụng biến môi trường hoặc file properties).
