# Card Payment Processor

A reactive microservice for processing card payments, built with Kotlin, Spring Boot, and Apache Cassandra.

## Features

- **Card Management**: Create and manage card details with masked PANs.
- **Payment Lifecycle**: Complete payment flow including:
    - **Authorization**: Temporarily reserve funds on a card.
    - **Capture**: Finalize an authorized payment.
    - **Refund**: Return funds to the cardholder after capture.
    - **Reversal**: Cancel an authorization before capture.
- **Event-Driven Design**: Captures detailed payment events for audit trails and consistency.
- **Reactive Stack**: Fully non-blocking API using Spring WebFlux and Reactive Cassandra.
- **Data denormalization**: Optimized Cassandra schemas for various query patterns (by card, by status, by transaction).

## Technologies Used

- **Language**: Kotlin 2.3.21
- **Framework**: Spring Boot 4.1.0
- **Database**: Apache Cassandra 5.0
- **Asynchronous Engine**: Project Reactor & Kotlin Coroutines
- **Testing**: Testcontainers (Cassandra), Junit 5, WebTestClient
- **Containerization**: Docker & Docker Compose

## Prerequisites

- Java 21
- Docker & Docker Compose
- Gradle (included via wrapper)

## Getting Started

### 1. Start Cassandra Infrastructure

The project includes a `docker-compose.yml` that sets up a 2-node Cassandra cluster and automatically runs migrations.

```bash
docker-compose up -d
```

Migrations are located in `docker-config/cassandra/migration/` and handled by the `cassandra-init` service.

### 2. Build the Application

```bash
./gradlew build
```

### 3. Run the Application

```bash
./gradlew bootRun
```

The server will start on `http://localhost:8080`.

## API Endpoints

### Cards

- `POST /cards`: Create a new card.
    - Request body: `CardDto` (holder, pan, creditLimit, availableCredit, currency)

### Payments

- `POST /payments/authorize`: Authorize a payment.
    - Request body: `AuthorizePaymentDto` (cardId, amount, currency, merchant)
- `POST /payments/{id}/capture`: Capture a previously authorized payment.
- `POST /payments/{id}/refund`: Refund a captured payment.
- `POST /payments/{id}/reverse`: Reverse an authorized payment.

## Architecture

The project follows a **Clean Architecture** approach:

- **Controllers**: Entry points for HTTP requests.
- **Use Cases**: Encapsulate business logic and orchestrate data flow.
- **Entities**: Domain models mapped to Cassandra tables.
- **Repositories**: Reactive data access layer using Spring Data Cassandra.
- **Events**: Internal application events published during the payment lifecycle.

### Cassandra Data Model

Data is denormalized across several tables to support efficient queries:

- `cards`: Primary card information.
- `payments`: Single source of truth for transaction status.
- `payment_events_by_transaction`: Audit trail of all events for a transaction.
- `payments_by_card`: Payment history for a specific card.
- `authorizations_by_card`: Active authorizations by card.
- `transactions_by_status`: Transactions grouped by their current state.

## Testing

Integration tests use **Testcontainers** to spin up a real Cassandra instance during the test suite.

Run tests:

```bash
./gradlew test
```

Key test files:

- `PaymentIT.kt`: End-to-end payment lifecycle verification.
- `CardIT.kt`: Card creation and persistence verification.

## Monitoring

- **Actuator Health**: `http://localhost:8080/actuator/health`
- **Actuator Info**: `http://localhost:8080/actuator/info`
