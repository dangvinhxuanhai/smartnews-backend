# SmartNews Backend

SmartNews is a RESTful News Management System built with **Java 21** and **Spring Boot**. The backend provides authentication, role-based authorization, article management, category/tag management, image uploads, dynamic search, public news APIs, user administration, and dashboard statistics.

Repository: https://github.com/dangvinhxuanhai/smartnews-backend

## Tech Stack

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Security
- Spring Data JPA / Hibernate
- Jakarta Bean Validation
- JWT authentication with JJWT
- Microsoft SQL Server
- Cloudinary
- Spring Mail / Gmail SMTP
- Maven
- Lombok

## Main Features

### Authentication & Authorization

- User registration
- Email verification by token
- Login with JWT authentication
- BCrypt password hashing
- Stateless Spring Security configuration
- Role-based authorization with `Admin` and `Staff`
- Current-user profile endpoint
- Protected REST APIs with method-level authorization

### Article Management

- Create articles as Draft
- Read article details
- Update articles
- Delete articles
- Owner/Admin authorization for article modification
- Article image URL support
- Article view count
- Article statuses: `Draft`, `Pending`, `Published`, `Rejected`
- Many-to-many article/tag relationship

### Search, Filter & Pagination

SmartNews uses **Spring Data JPA Specifications** for dynamic article queries.

Supported filters:

- Keyword
- Category
- Author
- Status
- Tag
- Created date range

Supported article sorting modes:

- Default: newest first
- `oldest`
- `mostViewed`
- `recentlyUpdate`

Example:

```http
GET /api/articles/search?keyword=java&categoryId=1&status=Published&tagId=2&page=0&size=10&sortBy=mostViewed
```

### Public News API

Public endpoints expose only published articles and do not require authentication.

```http
GET /api/public/articles
GET /api/public/articles/{id}
```

Public article listing supports the same search/filter request model while forcing article status to `Published`.

### Category Management

- View categories
- Create categories
- Update categories
- Parent-child category structure
- Soft delete by setting `IsActive = false`
- Admin-only write operations

### Tag Management

- View tags
- Create tags
- Update tags
- Delete tags
- Admin-only write operations

### Image Upload

Article images can be uploaded to Cloudinary through:

```http
POST /api/upload
Content-Type: multipart/form-data
```

The returned URL can then be stored in the article `imageUrl` field.

### Admin User Management

Admin endpoints support:

- Paginated account listing
- Search/filter users
- View account details
- Enable/disable accounts
- Protection against administrative access by non-admin users

Example:

```http
GET /api/admin/users?keyword=staff&role=Staff&status=true&page=0&size=10
```

Update account status:

```http
PATCH /api/admin/users/{id}/status
Content-Type: application/json

{
  "status": false
}
```

### Admin Dashboard

The dashboard endpoint provides summary statistics including:

- Total articles
- Published articles
- Draft articles
- Total users
- Active users
- Total categories
- Total tags
- Total article views

```http
GET /api/admin/dashboard
```

### Validation & Error Handling

The project uses Jakarta Bean Validation for request DTOs and a centralized global exception handler.

Standard error response format:

```json
{
  "timestamp": "2026-10-18T16:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/auth/register",
  "validationErrors": {
    "email": "Email is invalid"
  }
}
```

Handled error categories include:

- Resource not found — `404`
- Forbidden operations — `403`
- Bad requests — `400`
- Validation errors — `400`
- Unexpected server errors — `500`

## Project Structure

```text
src/main/java/com/example/smartnews
│
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── enums
├── exception
├── repository
├── security
├── service
│   └── impl
├── specification
└── util
```

## Authentication Flow

```text
Register
   │
   ▼
Verification email
   │
   ▼
Verify account
   │
   ▼
Login
   │
   ▼
JWT token
   │
   ▼
Authorization: Bearer <token>
   │
   ▼
Protected API
```

Example authenticated request:

```http
Authorization: Bearer <your-jwt-token>
```

## Database

SmartNews uses **Microsoft SQL Server**.

Main tables:

```text
SystemAccount
Category
NewsArticle
Tag
ArticleTag
```

Relationships:

```text
SystemAccount
      │
      │ 1:N
      ▼
NewsArticle
      │
      ├──── N:1 ──── Category
      │
      └──── N:N ──── Tag
```

Database scripts are available in:

```text
database/schema.sql
database/sample-data.sql
```

`schema.sql` was generated from SQL Server, so local database file paths may need to be adjusted depending on your SQL Server installation.

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/dangvinhxuanhai/smartnews-backend.git
cd smartnews-backend
```

### 2. Requirements

Make sure the following are installed/configured:

- JDK 21
- SQL Server
- Maven, or use the included Maven Wrapper
- Gmail App Password for verification emails
- Cloudinary account

### 3. Configure environment variables

Use `.env.example` as a reference:

```env
DB_URL=jdbc:sqlserver://localhost:1433;databaseName=SmartNewsDB;encrypt=true;trustServerCertificate=true
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password

MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password

CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_cloudinary_api_key
CLOUDINARY_API_SECRET=your_cloudinary_api_secret

JWT_SECRET=your_jwt_secret
```

> `.env.example` is only a template. Spring Boot does not automatically load a `.env` file, so provide these values through your IDE run configuration, operating-system environment variables, or another environment-loading solution.

### 4. Create the database

Run the SQL script in:

```text
database/schema.sql
```

Optional sample data:

```text
database/sample-data.sql
```

### 5. Run the application

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

The API runs at:

```text
http://localhost:8080
```

## API Overview

### Authentication

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register a Staff account |
| POST | `/api/auth/login` | Public | Login and receive JWT |
| GET | `/api/auth/verify?token=...` | Public | Verify email address |
| GET | `/api/auth/me` | Authenticated | Get current user profile |

### Public Articles

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/public/articles` | Public | Search/list published articles |
| GET | `/api/public/articles/{id}` | Public | Get a published article |

### Articles

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/articles` | Authenticated | Create a Draft article |
| GET | `/api/articles` | Authenticated | Get published articles |
| GET | `/api/articles/search` | Authenticated | Search/filter articles |
| GET | `/api/articles/{id}` | Authenticated | Get article details |
| PUT | `/api/articles/{id}` | Owner/Admin | Update article |
| DELETE | `/api/articles/{id}` | Owner/Admin | Delete article |

### Categories

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/categories` | Public | Get categories |
| GET | `/api/categories/{id}` | Public | Get category details |
| POST | `/api/categories` | Admin | Create category |
| PUT | `/api/categories/{id}` | Admin | Update category |
| DELETE | `/api/categories/{id}` | Admin | Soft delete category |

### Tags

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/tags` | Public | Get tags |
| GET | `/api/tags/{id}` | Public | Get tag details |
| POST | `/api/tags` | Admin | Create tag |
| PUT | `/api/tags/{id}` | Admin | Update tag |
| DELETE | `/api/tags/{id}` | Admin | Delete tag |

### Upload

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/upload` | Authenticated | Upload an image to Cloudinary |

### Admin

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/admin/users` | Admin | Search/list users |
| GET | `/api/admin/users/{id}` | Admin | Get user details |
| PATCH | `/api/admin/users/{id}/status` | Admin | Enable/disable account |
| GET | `/api/admin/dashboard` | Admin | Get dashboard statistics |

## Roles

### Admin

- Manage categories
- Manage tags
- Manage user account status
- View administrative dashboard
- Update/delete any article

### Staff

- Register and verify an account
- Login with JWT
- Create articles
- Update/delete owned articles
- Search and view article content

## Current Development Status

The main backend functionality is implemented and ready for frontend integration. The next development phase is the React frontend.

Planned improvements:

- React frontend integration
- Swagger / OpenAPI documentation
- Unit and integration tests
- Structured application logging
- Additional production hardening and cleanup

## Author

**Dang Vinh Xuan Hai**

SmartNews is a learning/portfolio project focused on building a practical Java Spring Boot REST API with authentication, authorization, relational data modeling, search, and administrative features.
