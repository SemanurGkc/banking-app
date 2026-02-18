# 🏦 Banking App

A full-stack banking application built with Spring Boot and JavaScript.
Supports two user roles — **Admin** and **User** — each with a distinct experience.

---

## Features

### Admin
- View and manage all accounts
- Create new accounts
- Deposit and withdraw from any account
- View all transactions across all accounts with filters

### User
- View personal accounts and balances
- Deposit and withdraw from own accounts
- View personal transaction history

---

## Tech Stack

| Layer     | Technology                         |
|-----------|------------------------------------|
| Backend   | Java, Spring Boot, Spring Security |
| Database  | MySQL                              |
| Frontend  | HTML, CSS, JavaScript              |
| Auth      | Session-based (HTTP-only cookie)   |

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8+

### 1. Clone the repository
```bash
git clone https://github.com/SemanurGkc/banking-app.git
cd banking-app
```

### 2. Configure the database

Create a MySQL database:
```sql
CREATE DATABASE banking_app;
```

Then update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking_app
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
```

### 3. Run the backend
```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

### 4. Open the frontend

Open `front-end/dashboard.html` in your browser, or serve the `front-end/` folder with any static file server:
```bash
# Example with VS Code Live Server, or:
npx serve front-end
```

---

## Project Structure
```
banking-app/
├── front-end/
│   ├── shared.css
│   ├── shared.js
│   ├── dashboard.html
│   ├── accounts.html
│   ├── account.html
│   ├── transactions.html
│   ├── new-account.html
│   ├── change-password.html
│   ├── login.html
│   └── register.html
├── src/main/java/net/javaguides/banking_app/
│   ├── config/          # Security & data initialization
│   ├── controller/      # AccountController, AuthController, TransactionController
│   ├── dto/             # Request/Response objects
│   ├── entity/          # Account, User, Role, Transaction
│   ├── exception/       # Global exception handling
│   ├── mapper/          # Entity ↔ DTO mappers
│   ├── repository/      # JPA repositories
│   ├── security/        # CustomUserDetailsService
│   ├── service/         # Business logic (interface + impl)
│   └── BankingAppApplication.java
└── pom.xml
```

---

## Default Credentials

> You can create users via the `/register` page. Assign roles directly in the database.
```sql
UPDATE users SET role = 'ROLE_ADMIN' WHERE username = 'your_username';
```

---

## Credits

Built with the help of [Claude](https://claude.ai) by Anthropic.
