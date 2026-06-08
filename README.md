Biblioteka
A REST API for managing a library's books, members, and loan operations. Built with Spring Boot and MySQL.
Tech Stack

Java 21, Spring Boot 3.5
Spring Data JPA + MySQL
Spring Security (Basic Auth)
Spring Actuator + Prometheus + Grafana (observability)
Docker + Docker Compose
JUnit 5 + Mockito (unit tests)
GitHub Actions (CI pipeline)

Features

Book inventory management (CRUD)
Member management with membership types (Student, Faculty, Public)
Book checkout and return with automatic copy tracking
Optimistic locking on book records to handle concurrent checkouts
Global exception handling with structured error responses
Metrics exposed via /actuator/prometheus, visualised in Grafana

Running Locally
Prerequisites: Docker and Docker Compose
bashgit clone https://github.com/yourusername/biblioteka
cd biblioteka
cp .env.example .env   # fill in DB credentials
docker compose up --build
App runs on http://localhost:8080. Grafana on http://localhost:3000.
API Overview
MethodEndpointDescriptionGET/api/booksList all booksPOST/api/booksAdd a bookPUT/api/books/{id}Update a bookDELETE/api/books/{id}Delete a bookPOST/api/loans/checkoutCheckout a book to a memberPOST/api/loans/returnReturn a book
Running Tests
bash./mvnw test
6 unit tests covering checkout success, out-of-stock, inactive member, successful return, double-return prevention, and wrong member return.