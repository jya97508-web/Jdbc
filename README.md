# Student JDBC + MySQL + Java Servlet + HTML/CSS/JS

A beginner-friendly full-stack CRUD project.

## Architecture

Frontend (HTML/CSS/JavaScript)
        |
        | fetch()
        v
Java Servlet REST-like endpoints
        |
        | JDBC
        v
MySQL database

## Requirements

- JDK 17+
- Maven 3.9+
- MySQL 8+
- Apache Tomcat 10.1+

## 1. Create the database

Open MySQL Workbench or MySQL command line and run `database.sql`.

## 2. Set your MySQL password

Open:
`src/main/java/com/example/student/DBConnection.java`

Change:
`private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";`

Do not put your database password in frontend JavaScript.

## 3. Build

From the project folder:

`mvn clean package`

The WAR will be created at:

`target/student-jdbc-app.war`

## 4. Deploy to Tomcat

Copy the WAR into Tomcat's `webapps` folder.

Start Tomcat.

Open:

`http://localhost:8080/student-jdbc-app/`

## API endpoints

GET    /api/students
POST   /api/students
PUT    /api/students?id=1
DELETE /api/students?id=1

The frontend calls these endpoints using JavaScript `fetch()`.

## Important

This project uses PreparedStatement for values supplied by the user.
For learning, the DB password is stored in Java. For production, use environment variables/secrets.
