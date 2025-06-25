# 📇 Smart Contact Manager

A full-stack web application that allows users to manage their personal contacts securely and efficiently. Built with **Spring Boot**, **Java**, **MySQL**, and a responsive **Bootstrap 5** frontend.

> 🔗 GitHub Repo: [Smart Contact Manager](https://github.com/SamK1828/Smart-Contact-Manager.git)

---

## 🛠️ Tech Stack

| Layer      | Technologies Used                          |
|------------|---------------------------------------------|
| **Frontend**  | HTML, CSS, JavaScript, Bootstrap 5, Thymeleaf |
| **Backend**   | Java 17, Spring Boot, Spring MVC, Spring Security |
| **Database**  | MySQL 8.x                                 |
| **Tools**     | Maven, Git, GitHub, IntelliJ/VSCode       |

---

## ✨ Features

- 🧾 **User Registration & Login**
- 🔒 **Spring Security-based Authentication**
- 👤 **Role-based Dashboard**
- 📇 **Create, Read, Update, Delete Contacts**
- 📁 **File Upload for Profile & Contacts**
- 🔍 **Search & Pagination (planned)**
- 🌐 **Responsive Bootstrap UI**
- 🧱 **Modular Code (Controller - Service - DAO Layers)**

---

## 🧑‍💻 Setup Instructions

### ✅ Prerequisites

- Java 17+
- Maven
- MySQL
- IDE (IntelliJ / VS Code / Eclipse)

### 📦 Clone the Repository

```bash
git clone https://github.com/SamK1828/Smart-Contact-Manager.git
cd Smart-Contact-Manager
````

### ⚙️ Configure Database

* Create a new MySQL database:

```sql
CREATE DATABASE smart_contact_manager;
```

* Update your `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smart_contact_manager
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

### 🚀 Run the Application

```bash
./mvnw spring-boot:run
```

App will be running at [http://localhost:8282](http://localhost:8282)

---

## 📂 Project Structure

```
src/
 └── main/
     ├── java/
     │    └── com.smartcontactmanager/
     │         ├── controller/
     │         ├── service/
     │         ├── repository/
     │         ├── entity/
     │         └── SmartContactManagerApplication.java
     └── resources/
          ├── templates/      # Thymeleaf HTML files
          ├── static/         # CSS, JS, images
          └── application.properties
```

---

## 🔐 Login Details (Dev)

> Default login credentials can be configured via database or CommandLineRunner

| Role  | Username | Password |
| ----- | -------- | -------- |
| User  | user     | password |
| Admin | admin    | admin123 |

---

## 📌 Future Enhancements

* 🔔 Email Notifications
* 🌍 REST APIs for contacts
* 📊 Contact Analytics
* 🛡️ OAuth2 or JWT Security

---

## 🙌 Contributing

1. Fork the repo 🍴
2. Create your branch (`git checkout -b feature-x`)
3. Commit your changes (`git commit -m 'Add feature'`)
4. Push to the branch (`git push origin feature-x`)
5. Open a Pull Request ✔️

---

## 📃 License

This project is open source and available under the [MIT License](LICENSE).

---

## ✍️ Author

* **Samarth Deelip Kalegaonkar**
  GitHub: [@SamK1828](https://github.com/SamK1828)
  Project Mentor: [YouTube Playlist](https://youtube.com/playlist?list=PL0zysOflRCelYeugqOJszoof1ruhLVdJc)
