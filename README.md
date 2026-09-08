# Limit Order Book & Matching Engine

A matching engine built with Spring Boot, replicating the core system of financial exchanges (Euronext, Binance).

## Tech Stack

Java 21, Spring Boot 4.1, Maven, Lombok

## Run locally

```bash
git clone https://github.com/YassineBen005/order_book.git
cd order_book
./mvnw spring-boot:run
```

## API Endpoints

| Method | URL | Description |
|---|---|---|
| POST | /api/orders | Submit a new order |
| GET | /api/orders | Get current order book |
| DELETE | /api/orders/{id} | Cancel an order |

### Submit an order

```http
POST /api/orders
Content-Type: application/json

{
  "price": 150.0,
  "quantity": 10,
  "side": "BUY"
}
```

## Matching Algorithm

Price-time priority: bids sorted by descending price, asks by ascending price. Supports partial fills.

## Roadmap

- Docker + CI/CD with GitHub Actions
- WebSocket for real-time trade streaming
- PostgreSQL for trade history persistence
