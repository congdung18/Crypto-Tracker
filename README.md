# Crypto Tracker API

## Introduction
A robust backend application built with Spring Boot that allows users to search for cryptocurrency information and track market trends in real-time.

## Technology Stack
* **Language:** Java 25
* **Framework:** Spring Boot 4.0.2
* **Database:** PostgreSQL 18-alpine
* **API Integration:** CoinGecko API
* **Build tool:** Maven 4.0.0 with Maven Wrapper
* **Testing:** JUnit5, Mockito, Testcontainers.

## Main Features
- [X] **Real-time Tracking:** Fetch and display live cryptocurrency prices.
- [X] **Search:** Search for specific coins by name or symbol (BTC, ETH, etc.).
- [X] **Testing:** Achieved 87% test coverage according to JaCoCo.
- [x] **Optimize queries:** Applied Redis caching (Key strategy for ID) to reduce API response time from ~200ms to <10ms.
- [ ] **Watchlist:** Allow users to save their favorite coins to a personalized list.
- [ ] **Historical data:** View price charts for the last 24 hours.
- [ ] **Optimize queries:** Apply indexing and Redis caching for better query time.
- [ ] **ML models prediction:** Integrate a model to provide prediction for potential currencies.

## ⚙️ How to Run
*Set up Infrastructure:* type ``docker compose up -d`` to turn on the databases.
Now you have a PostgreSQL instance running on port 5440.

*Run:* type ``./mvnw spring-boot:run``.

Now the application has started on your **8088 (default)** port or your ``/src/main/java/resources/application.properties`` **server.port**.

## Endpoints
- *api/v1/coins*: Track all coins available.
- *api/v1/global*: Overview about the markets right now.

---
*Developed as part of a Team project for research purposes.*
