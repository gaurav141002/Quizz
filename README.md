# QuizApp (Spring Boot) - MySQL

## Overview
Simple Quiz API with:
- Create quizzes
- Add questions
- Fetch quiz questions (without correct answers)
- Submit answers and get score

## Setup (MySQL)
1. Create a MySQL database:
   ```sql
   CREATE DATABASE quizdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. Edit `src/main/resources/application.properties` and set `spring.datasource.username` and `spring.datasource.password`.

3. Build & run:
   ```bash
   mvn clean package
   mvn spring-boot:run
   ```

## Endpoints
- `POST /api/quizzes` `{ "title":"My Quiz" }`
- `GET /api/quizzes`
- `POST /api/quizzes/{quizId}/questions` (see DTO in code)
- `GET /api/quizzes/{quizId}/questions`
- `POST /api/quizzes/{quizId}/submit` (submit answers)

## Notes
- Java 17, Spring Boot 3.x
- Uses `spring.jpa.hibernate.ddl-auto=update` for convenience (change for production).
- Tests included for scoring logic.
# Quizz
