# AI-Powered Task Management Portal — Backend

This is the backend for a full-stack task management application built as part of an internship assignment for Future Transformation. The app lets users register, log in, create and manage tasks, and use AI to auto-generate task descriptions and priorities.

---

## Live URLs

- **API Base:** https://task-management-backend-latest.onrender.com
- **Swagger UI:** https://task-management-backend-latest.onrender.com/swagger-ui/index.html
- **Frontend:** https://task-management-frontend-phi-ten.vercel.app

---

## What it does

- Users can register and log in with JWT-based authentication
- Each user can create, view, edit, delete, and update the status of their tasks
- An AI feature generates a task description, priority, and effort estimate from just a task title
- All APIs are documented and testable via Swagger UI

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Security | Spring Security + JWT |
| Database | MySQL (hosted on Railway) |
| ORM | Spring Data JPA + Hibernate |
| AI | Groq API (llama-3.3-70b-versatile) |
| Containerization | Docker |
| Deployment | Render |

---

## Project Structure
src/main/java/com/futuretransformation/taskmanagement/
│
├── controller/        # REST API endpoints
├── service/           # Business logic
├── repository/        # Database access
├── model/             # JPA entities (User, Task)
├── dto/               # Request/Response objects
├── security/          # JWT filter, config
└── exception/         # Global error handling

---

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/auth/register | Register a new user |
| POST | /api/auth/login | Login and receive JWT token |

### Tasks (protected — requires Bearer token)
| Method | Endpoint | Description |
|---|---|---|
| GET | /api/tasks | Get all tasks for logged-in user |
| POST | /api/tasks | Create a new task |
| GET | /api/tasks/{id} | Get a specific task |
| PUT | /api/tasks/{id} | Update a task |
| DELETE | /api/tasks/{id} | Delete a task |

### AI
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/ai/generate | Generate description, priority and effort from task title |

---

## AI Integration

The AI feature uses the Groq API with the `llama-3.3-70b-versatile` model.

When a user sends a task title, the backend calls Groq and returns:
- A one-line task description
- A suggested priority (LOW, MEDIUM, or HIGH)
- An estimated effort (e.g. "2 hours", "1 day")

If the AI call fails for any reason, the API returns a sensible default response instead of throwing an error.

---

## Database Schema

**users**
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | Primary key |
| name | VARCHAR(100) | |
| email | VARCHAR(150) | Unique |
| password | VARCHAR(255) | BCrypt hashed |
| created_at | TIMESTAMP | Auto-set |

**tasks**
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | Primary key |
| user_id | BIGINT | Foreign key → users |
| title | VARCHAR(255) | |
| description | TEXT | |
| priority | ENUM | LOW, MEDIUM, HIGH |
| status | ENUM | TODO, IN_PROGRESS, DONE |
| due_date | DATE | |
| created_at | TIMESTAMP | Auto-set |

---

## Running Locally

**Prerequisites:** Java 17, Maven, MySQL

```bash
git clone https://github.com/Sukesh-blip/task-management-backend
cd task-management-backend
```

Create `src/main/resources/application-secrets.properties`:
```properties
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_secret_key_minimum_32_characters
GROQ_API_KEY=your_groq_api_key
```

Run:
```bash
./mvnw spring-boot:run
```

API will be available at `http://localhost:8080`

---

## Running with Docker

```bash
docker build -t task-management-backend .
docker run -p 8080:8080 task-management-backend
```

---

## Assumptions

- Each user can only see and manage their own tasks
- JWT tokens expire after 24 hours
- AI generation is optional — tasks can be created without it
- Password is hashed using BCrypt before storage
