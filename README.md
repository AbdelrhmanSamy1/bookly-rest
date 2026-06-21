# Bookly REST API

A production-ready bookstore REST API built with Spring Boot 3, featuring JWT authentication, role-based access control, and dynamic filtering.

🔗 **Live API:** https://bookly-rest-production.up.railway.app/swagger-ui.html

---

## Tech Stack

| | |
|---|---|
| Framework | Spring Boot 3 + Java 21 |
| Database | MySQL 8 + Spring Data JPA |
| Security | Spring Security + JWT |
| Mapping | MapStruct |
| Docs | Swagger / OpenAPI 3 |
| DevOps | Docker + Railway |

---

## Features

- **CRUD** for Books, Authors, Categories, Orders, and Users
- **JWT Authentication** with Refresh Token Rotation and reuse detection
- **Role-Based Access Control** — ADMIN and USER roles
- **Dynamic Filtering** via JPA Specifications — filter books by title, author, category, price range, and stock
- **Pagination & Sorting** on all list endpoints
- **Global Exception Handling** with structured error responses
- **Auditing** — automatic `createdAt` / `updatedAt` on all entities
- **Dockerized** — runs with a single command

---

## Getting Started

### Prerequisites
- Docker Desktop
- Java 21

### Run locally

```bash
git clone https://github.com/AbdelrhmanSamy1/bookly-rest.git
cd bookly-rest
docker-compose up --build
```

API will be available at `http://localhost:8080`
Swagger UI at `http://localhost:8080/swagger-ui.html`

---

## API Overview

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/v1/auth/register` | Public |
| POST | `/api/v1/auth/login` | Public |
| POST | `/api/v1/auth/refresh` | Public |
| POST | `/api/v1/auth/logout` | Authenticated |
| GET | `/api/v1/books/search` | Public |
| POST | `/api/v1/books` | ADMIN |
| GET | `/api/v1/orders/user/{id}` | Authenticated |
| PATCH | `/api/v1/orders/{id}/status` | ADMIN |

Full documentation available on the live Swagger UI.

---

## Project Structure

```
src/
├── controllers/     # REST endpoints
├── Services/        # Business logic
├── repository/      # Data access
├── entity/          # JPA entities
├── dto/             # Request & response objects
├── mapper/          # MapStruct mappers
├── security/        # JWT filter, config
├── specification/   # Dynamic filtering
└── exception/       # Global error handling
```

---

## Author

**Abdelrhman Samy** — [LinkedIn](https://linkedin.com/in/abdelrhman-samy-908258226) · [GitHub](https://github.com/AbdelrhmanSamy1)
