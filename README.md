# Full-Stack Boilerplate: React + Spring Boot + PostgreSQL

A production-ready boilerplate using **React (Vite)**, **Java Spring Boot 3**, and **PostgreSQL 16**. Demonstrates a full CRUD Todo application wired end-to-end.

---

## Project Structure

```
.
├── frontend/          # React 18 + Vite
│   ├── src/
│   │   ├── components/    # TodoForm, TodoList, TodoItem
│   │   ├── services/      # Axios API layer (todoService.js)
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── Dockerfile
│   ├── nginx.conf
│   └── vite.config.js     # Proxy /api → localhost:8080
│
├── backend/           # Spring Boot 3 + Java 21
│   ├── src/main/java/com/example/app/
│   │   ├── controller/    # TodoController (REST)
│   │   ├── service/       # TodoService
│   │   ├── repository/    # TodoRepository (JPA)
│   │   ├── model/         # Todo entity
│   │   └── exception/     # GlobalExceptionHandler
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── src/test/          # Unit tests (Mockito)
│   ├── Dockerfile
│   └── pom.xml
│
├── db/
│   └── init.sql           # Initial schema
├── docker-compose.yml
└── README.md
```

---

## Quick Start (Docker)

```bash
docker-compose up --build
```

| Service   | URL                    |
|-----------|------------------------|
| Frontend  | http://localhost:3000  |
| Backend   | http://localhost:8080  |
| Postgres  | localhost:5432         |

---

## Local Development (without Docker)

### Prerequisites
- Node.js 20+
- Java 21 + Maven 3.9+
- PostgreSQL 16 running locally

### 1. Start the database

```sql
CREATE DATABASE appdb;
CREATE USER appuser WITH PASSWORD 'apppassword';
GRANT ALL PRIVILEGES ON DATABASE appdb TO appuser;
```

### 2. Run the backend

```bash
cd backend
mvn spring-boot:run
# Runs on http://localhost:8080
```

### 3. Run the frontend

```bash
cd frontend
npm install
npm run dev
# Runs on http://localhost:3000
```

---

## API Reference

| Method | Endpoint         | Description       |
|--------|------------------|-------------------|
| GET    | /api/todos       | List all todos    |
| GET    | /api/todos/{id}  | Get a single todo |
| POST   | /api/todos       | Create a todo     |
| PUT    | /api/todos/{id}  | Update a todo     |
| DELETE | /api/todos/{id}  | Delete a todo     |

### Example payload

```json
{ "title": "Buy groceries", "completed": false }
```

---

## Running Tests

```bash
cd backend
mvn test
```

---

## Tech Stack

| Layer     | Technology                     |
|-----------|--------------------------------|
| Frontend  | React 18, Vite, Axios          |
| Backend   | Spring Boot 3, Spring Data JPA |
| Database  | PostgreSQL 16                  |
| Container | Docker, Docker Compose, Nginx  |