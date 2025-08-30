# FundMate Backend

The **FundMate Backend** is built using **Spring Boot (Java 17)** with
**MongoDB Atlas** as the database.\
It provides secure APIs for user authentication, fund management, and
financial tracking.\
The backend is designed to connect seamlessly with the FundMate React
frontend.

------------------------------------------------------------------------

## 🚀 Features

-   **User Management**
    -   User registration & login (basic authentication)
    -   Profile management
-   **Fund Management**
    -   Add, update, and delete funds
    -   Store notes and transaction details
-   **Analytics**
    -   Generate reports and summaries
    -   Provide data for charts (line & pie charts)
-   **CORS Support**
    -   Configured to work with React frontend (Netlify deployment)
-   **Optimized for Low Memory Deployment**
    -   Tomcat thread pool reduced
    -   Lazy initialization enabled
    -   Custom JVM options for 512MB RAM environments

------------------------------------------------------------------------

## 🛠️ Tech Stack

-   **Java 17**
-   **Spring Boot 3**
-   **MongoDB Atlas**
-   **Maven**
-   **Deployed on Railway.app**

------------------------------------------------------------------------

## ⚙️ Deployment Settings

### application.properties optimizations

``` properties
spring.main.lazy-initialization=true
server.tomcat.threads.max=20
server.tomcat.threads.min-spare=5
```

### Railway Deployment Command

``` bash
java -Xms64m -Xmx192m -Xss256k -XX:MaxMetaspaceSize=64m -jar target/fundmate-0.0.1-SNAPSHOT.jar
```

------------------------------------------------------------------------

## 📂 Project Structure

    fundmate-backend/
    │── src/main/java/com/fundmate/
    │   ├── controller/   # REST Controllers
    │   ├── service/      # Business Logic
    │   ├── repository/   # MongoDB Repositories
    │   └── model/        # Data Models
    │
    │── src/main/resources/
    │   ├── application.properties
    │
    │── pom.xml           # Maven Dependencies

------------------------------------------------------------------------

## 🔗 Frontend

The backend connects with the FundMate frontend (React + Vite +
Tailwind) hosted on **Netlify**.

------------------------------------------------------------------------

## 📌 Notes

-   Keep memory usage optimized for Railway free tier (512MB).
-   Future scope: Add JWT authentication for better security.
