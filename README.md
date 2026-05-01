# MeterFlow — Backend

Spring Boot backend for MeterFlow, a usage-based API billing platform.

## Live URL
https://meterflow-backend-kbyx.onrender.com

## Tech Stack
- Java 21, Spring Boot 3.3.6
- PostgreSQL (Render)
- JWT + Spring Security
- Docker (deployed on Render)

## API Endpoints
POST   /api/auth/register
POST   /api/auth/login
POST   /api/apis
GET    /api/apis
DELETE /api/apis/{id}
POST   /api/apis/{id}/keys
GET    /api/billing/current
GET    /api/billing/history
POST   /api/billing/calculate
GET    /api/usage/summary
GET    /api/usage/logs
GET    /gateway/** (X-API-KEY header required)

## Setup Locally
- Java 21, Maven, PostgreSQL required
- Create database: meterflow
- Update application.properties with your DB credentials
- Run: ./mvnw spring-boot:run
