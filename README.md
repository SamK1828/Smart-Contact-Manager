# Smart Contact Manager

A Spring Boot web application for keeping a private, account-scoped contact list.

## Features

- Account registration with validation and BCrypt password hashing
- Email/password sign-in and sign-out
- Create, view, update, and delete personal contacts
- Profile details and password settings
- Responsive Thymeleaf interface
- CSRF protection and per-account contact ownership checks

Image uploads, search and pagination, and administrative tools are not included in this MVP.

## Requirements

- Java 24
- MySQL

## Run locally

From the repository root, enter the Maven project:

```powershell
cd smartcontactmanager
```

Set the connection settings for your local MySQL instance. The defaults use port `3307`, database `smartcontactmanager`, user `root`, and a blank password; override them as appropriate:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3307/smartcontactmanager?serverTimezone=UTC"
$env:DB_USERNAME = "your_mysql_username"
$env:DB_PASSWORD = "your_mysql_password"
.\mvnw.cmd spring-boot:run
```

On macOS or Linux, use `./mvnw spring-boot:run` after setting the same environment variables.

The app is available at [http://localhost:8282](http://localhost:8282). The development schema is synchronized by Hibernate. For a production database, manage schema migrations separately and set `DDL_AUTO=validate`.

## Run tests

```powershell
cd smartcontactmanager
.\mvnw.cmd test
```

Tests use an in-memory H2 database and do not require a local MySQL server.

## Project layout

```text
smartcontactmanager/
  src/main/java/com/smart/
    config/       Spring Security and user authentication
    controller/   Web routes
    dao/          Spring Data repositories
    models/       Persistence models and form objects
    service/      Contact ownership and operations
  src/main/resources/
    static/       CSS, JavaScript, and images
    templates/    Thymeleaf pages
  src/test/       Application and behavior tests
```
