# SmartNews Backend

SmartNews is a RESTful News Management System built with Spring Boot.

The project provides authentication, role-based authorization, article management, categories, tags, image uploads, and flexible article search and filtering.

## Technologies

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT Authentication
- SQL Server
- Cloudinary
- Maven

## Features

### Authentication & Authorization

- User registration
- Email verification
- Login with JWT authentication
- Password encryption with BCrypt
- Role-based authorization
- Admin and Staff roles
- Protected REST APIs

### Article Management

- Create articles
- View articles
- Update articles
- Delete articles
- Draft and Published article status
- Article ownership authorization
- Article view count
- Article image upload with Cloudinary

### Search & Filtering

Articles can be searched and filtered by:

- Keyword
- Category
- Author
- Status
- Tag
- Created date

The API also supports pagination and sorting.

### Category Management

- Create categories
- Update categories
- View categories
- Parent-child category structure
- Soft delete categories

### Tag Management

- Create tags
- Update tags
- View tags
- Delete tags

## Project Structure

```text
src/main/java/com/example/smartnews
│
├── config
├── controller
├── dto
├── entity
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
   |
   v
Email Verification
   |
   v
Login
   |
   v
JWT Token
   |
   v
Authorization Header
   |
   v
Protected API
```

Example:

```http
Authorization: Bearer <your-jwt-token>
```

## Article Search

SmartNews uses Spring Data JPA Specifications for dynamic article queries.

Supported filters include:

```text
keyword
category
author
status
tag
fromDate
toDate
```

Example:

```http
GET /api/articles/search?keyword=java&categoryId=1&page=0&size=10
```

## Database

The project uses Microsoft SQL Server.

Main entities:

```text
SystemAccount
Category
NewsArticle
Tag
ArticleTag
```

Main relationships:

```text
SystemAccount
      |
      | 1:N
      v
NewsArticle
      |
      +---- N:1 ---- Category
      |
      +---- N:N ---- Tag
```

## Image Upload

Article images are uploaded to Cloudinary.

The returned image URL is stored in the `NewsArticle` entity.

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd smartnews-backend
```

### 2. Configure environment variables

Create your local environment configuration based on `.env.example`.

Required configuration:

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=

MAIL_USERNAME=
MAIL_PASSWORD=

CLOUDINARY_CLOUD_NAME=
CLOUDINARY_API_KEY=
CLOUDINARY_API_SECRET=

JWT_SECRET=
```

### 3. Create the database

Create a SQL Server database:

```sql
CREATE DATABASE SmartNewsDB;
```

Configure the database connection using your environment variables.

### 4. Run the application

Using Maven Wrapper:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register account |
| GET | `/api/auth/verify` | Verify email |
| POST | `/api/auth/login` | Login |
| GET | `/api/auth/me` | Get current user |
| GET | `/api/articles` | Get articles |
| GET | `/api/articles/{id}` | Get article details |
| POST | `/api/articles` | Create article |
| PUT | `/api/articles/{id}` | Update article |
| DELETE | `/api/articles/{id}` | Delete article |
| GET | `/api/articles/search` | Search articles |
| GET | `/api/categories` | Get categories |
| POST | `/api/categories` | Create category |
| GET | `/api/tags` | Get tags |
| POST | `/api/tags` | Create tag |

## Roles

### Admin

- Manage articles
- Manage categories
- Manage tags
- Access administrative operations

### Staff

- Create articles
- Update owned articles
- Delete owned articles
- Manage their article content

## Future Improvements

- Public news APIs
- User management
- Admin dashboard
- News statistics
- Improved exception handling
- Swagger / OpenAPI documentation
- Unit and integration tests

## Author

**Dang Vinh Xuan Hai**

Backend project developed for practicing Java and Spring Boot REST API development.
