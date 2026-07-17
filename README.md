# 📚 Bookly — Book Store REST API

A production-ready RESTful API for a bookstore, built with **Spring Boot 3** and deployed on **Railway**.

> 🔗 **Live API:** [`bookly-rest-production.up.railway.app`](https://bookly-rest-production.up.railway.app)
>
> 📖 **Swagger Docs:** [`/swagger-ui.html`](https://bookly-rest-production.up.railway.app/swagger-ui.html)

---

## ✨ Features

| Feature | Description |
|---|---|
| **JWT Authentication** | Register/Login with access & refresh token rotation |
| **Book Management** | Full CRUD with pagination, sorting, and advanced search filters |
| **Order System** | Place orders, track status (PENDING → CONFIRMED → SHIPPED → DELIVERED), stock validation |
| **Author & Category** | Manage authors and categories with relationships |
| **Role-Based Access** | `ADMIN` and `USER` roles with endpoint-level security |
| **Swagger/OpenAPI** | Auto-generated interactive API docs with example payloads |
| **Global Error Handling** | Consistent error responses across all endpoints |

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Framework** | Spring Boot 3.5 |
| **Language** | Java 21 |
| **Database** | PostgreSQL (Railway) |
| **ORM** | Spring Data JPA / Hibernate |
| **Security** | Spring Security + JWT (jjwt) |
| **Docs** | SpringDoc OpenAPI 3 (Swagger UI) |
| **Build** | Maven |
| **Deployment** | Railway |

## 📐 Architecture

```
src/main/java/com/example/bookly/
├── config/           # Swagger, CORS, Data Seeder
├── controllers/      # REST endpoints (6 controllers, 30+ endpoints)
├── dto/              # Request/Response DTOs with validation
│   ├── request/      # @Schema-annotated request bodies
│   └── response/     # Response projections
├── entity/           # JPA entities (Book, Author, Category, Order, User)
├── exception/        # Custom exceptions + GlobalExceptionHandler
├── mapper/           # Entity ↔ DTO mappers
├── repository/       # Spring Data repositories
├── security/         # JWT filter, UserDetailsService, SecurityConfig
└── Services/         # Business logic layer
```

## 🚀 API Endpoints

### 🔐 Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/auth/register` | Create a new account |
| `POST` | `/api/v1/auth/login` | Login & get JWT tokens |
| `POST` | `/api/v1/auth/refresh` | Refresh access token |
| `POST` | `/api/v1/auth/logout` | Revoke refresh token |

### 📖 Books
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/books` | Get all books |
| `GET` | `/api/v1/books/{id}` | Get book by ID |
| `GET` | `/api/v1/books/search` | Search with filters (title, author, category, price range, stock) |
| `POST` | `/api/v1/books` | Create book 🔒 |
| `PUT` | `/api/v1/books/{id}` | Update book 🔒 |
| `DELETE` | `/api/v1/books/{id}` | Delete book 🔒 |

### ✍️ Authors
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/authors` | Get all authors |
| `GET` | `/api/v1/authors/{id}` | Get author by ID |
| `POST` | `/api/v1/authors` | Create author 🔒 |
| `PUT` | `/api/v1/authors/{id}` | Update author 🔒 |
| `DELETE` | `/api/v1/authors/{id}` | Delete author 🔒 |

### 📂 Categories
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/categories` | Get all categories |
| `GET` | `/api/v1/categories/{id}` | Get category by ID |
| `POST` | `/api/v1/categories` | Create category 🔒 |
| `PUT` | `/api/v1/categories/{id}` | Update category 🔒 |
| `DELETE` | `/api/v1/categories/{id}` | Delete category 🔒 |

### 🛒 Orders
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/orders` | Get all orders 🔒 |
| `GET` | `/api/v1/orders/{id}` | Get order by ID 🔒 |
| `GET` | `/api/v1/orders/user/{userId}` | Get orders by user 🔒 |
| `POST` | `/api/v1/orders` | Place a new order 🔒 |
| `PUT` | `/api/v1/orders/{id}/status` | Update order status 🔒 |

> 🔒 = Requires JWT Bearer token

## 🔎 Search & Filtering

The `/api/v1/books/search` endpoint supports:

```
GET /api/v1/books/search?title=harry&categoryName=Fantasy&minPrice=10&maxPrice=20&inStock=true&page=0&size=10&sortBy=price&sortDir=asc
```

| Parameter | Type | Description |
|-----------|------|-------------|
| `title` | String | Partial match on book title |
| `authorName` | String | Filter by author name |
| `categoryName` | String | Filter by category name |
| `minPrice` / `maxPrice` | Double | Price range |
| `publishedYear` | Integer | Exact year match |
| `inStock` | Boolean | Only show books in stock |
| `page` / `size` | Integer | Pagination |
| `sortBy` / `sortDir` | String | Sort field and direction |

## ⚙️ Run Locally

```bash
# Clone
git clone https://github.com/AbdelrhmanSamy1/bookly-rest.git
cd bookly-rest

# Set environment variables (or use application.properties)
export DB_URL=jdbc:postgresql://localhost:5432/bookly
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword
export JWT_SECRET=your-256-bit-secret

# Run
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080` and Swagger at `http://localhost:8080/swagger-ui.html`.

## 📦 Sample Data

The app auto-seeds the database on first run with:
- **6 categories** — Fantasy, Science Fiction, Mystery, Romance, Horror, Non-Fiction
- **10 authors** — Tolkien, Rowling, Asimov, Christie, Austen, King, Orwell, Herbert, Sanderson, Clear
- **20 books** — Real titles with descriptions, ISBNs, prices, and stock quantities

## 📱 Flutter Client

A companion **Flutter mobile app** is available at [`AbdelrhmanSamy1/bookly`](https://github.com/AbdelrhmanSamy1/bookly) — built with Riverpod, Dio, and go_router.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
