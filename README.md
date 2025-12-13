## 🏗️ Kiến trúc hệ thống (Architecture Flow)

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
