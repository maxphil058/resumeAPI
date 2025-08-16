# resumeAPI — Spring Boot Backend

A Spring Boot (WebFlux) API that tailors resumes to a given job description using OpenAI.  
This backend powers the frontend at **https://resume-generator-smab.onrender.com**.

---

## ✨ Features
- Java 17+ / Spring Boot 3.x (WebFlux, Validation, Actuator)
- OpenAI Chat Completions (`gpt-3.5-turbo`)
- REST endpoint: `POST /api/jobDesc`
- Configured CORS for local dev (`http://localhost:5173`) and production frontend (Render)
- Dockerfile + Render-ready start command

---

## 🧱 Tech Stack
- **Language:** Java (17+)
- **Framework:** Spring Boot 3.x
- **Build Tool:** Maven (wrapper included)
- **HTTP Client:** Spring `WebClient`
- **Deployment:** Render (Docker or Jar)

---

## 📂 API Endpoints

### `POST /api/jobDesc`
Tailors a resume (plain text) to a job description (plain text).

**Request body (JSON)**
{
  "jobDescription": "Paste the job posting here...",
  "resumeText": "Paste your resume text here..."
}

**Response (200 OK)**
Rewritten resume text...

**Error cases**
- 400 Bad Request → invalid/missing fields  
- 401 Unauthorized → missing/invalid OPENAI_API_KEY  
- 5xx → error from OpenAI or backend  

---

### Health Check
GET /actuator/health

Response:
{"status":"UP"}

---

## 🔐 CORS
Allowed origins:
- http://localhost:5173
- https://resume-generator-smab.onrender.com

If you deploy the frontend at a new domain, update the @CrossOrigin annotation in ResumeAPIController.

---

## ⚙️ Configuration

### Environment Variables
Variable         | Required | Example   | Purpose
---------------- | -------- | --------- | -------------------------
OPENAI_API_KEY   | ✅       | sk-...    | OpenAI API key
PORT             | Render   | 8080      | Render sets this; local default 8080

application.properties
server.port=${PORT:8080}
openai.api.key=${OPENAI_API_KEY:}

---

## ▶️ Run Locally

Prerequisites
- Java 17+
- Maven (or ./mvnw wrapper)

Start backend
export OPENAI_API_KEY="sk-..."
./mvnw spring-boot:run

Runs at http://localhost:8080

Test endpoint
curl -X POST http://localhost:8080/api/jobDesc \
  -H "Content-Type: application/json" \
  -d '{"jobDescription":"Senior Java Developer role...", "resumeText":"My resume content..."}'

Check health
curl http://localhost:8080/actuator/health

Build jar
./mvnw clean package
java -jar target/resumeAPI-0.0.1-SNAPSHOT.jar

---

## 🐳 Docker

The included Dockerfile is multi-stage (Maven build + lightweight runtime).

Build & run locally
docker build -t resumeapi .
docker run --rm -p 8080:8080 -e OPENAI_API_KEY="sk-..." resumeapi

---

## ☁️ Deploy to Render

Option 1: Build with Maven
- Build Command: ./mvnw clean package
- Start Command: java -jar target/resumeAPI-0.0.1-SNAPSHOT.jar
- Health Check Path: /actuator/health
- Env Vars: set OPENAI_API_KEY

Option 2: Deploy with Dockerfile
- Render auto-detects and uses the included Dockerfile.

Notes on Free Plan
- Render free instances sleep after ~15 min idle.
- Use a free uptime monitor (e.g. UptimeRobot) to ping /actuator/health every 5 min if you want it warm.

---

## 🧠 OpenAI Integration

WebClientConfig.java
@Bean
public WebClient webClient(@Value("${openai.api.key}") String apiKey) {
    return WebClient.builder()
        .baseUrl("https://api.openai.com/v1")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
        .build();
}

ResumeAIService.java
- Builds a prompt with the job description + resume
- Calls POST /chat/completions with model gpt-3.5-turbo
- Returns the tailored resume as plain text

---

## 🛠️ Troubleshooting
- CORS error in frontend → ensure frontend origin is in @CrossOrigin
- 401 Unauthorized → check OPENAI_API_KEY
- 404 Not Found → confirm frontend calls /api/jobDesc, not /jobDesc
- Port issues on Render → ensure server.port=${PORT:8080} is set (already configured)
- Service sleeps → free Render plan goes idle; use uptime monitor if needed

---
