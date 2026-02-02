# System Architecture Diagram

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         FRONTEND (React)                         │
│                      http://localhost:3000                       │
│                                                                  │
│  ┌──────────────┐  ┌──────────────────┐  ┌──────────────────┐ │
│  │    Login     │  │ Seller Dashboard │  │ Buyer Dashboard  │ │
│  │  Component   │  │   - List Books   │  │  - Browse Books  │ │
│  │              │  │   - View Sales   │  │  - Purchase      │ │
│  └──────────────┘  └──────────────────┘  └──────────────────┘ │
└────────────────────────────┬─────────────────────────────────────┘
                             │ HTTP Requests
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      API GATEWAY (Port 8080)                     │
│                   Spring Cloud Gateway                           │
│                                                                  │
│  Routes:                                                         │
│  • /api/users/**   → User Service                              │
│  • /api/books/**   → Book Service                              │
│  • /api/orders/**  → Order Service                             │
└────────────┬──────────────┬──────────────┬──────────────────────┘
             │              │              │
             ▼              ▼              ▼
┌──────────────────┐ ┌──────────────┐ ┌──────────────────┐
│  USER SERVICE    │ │ BOOK SERVICE │ │  ORDER SERVICE   │
│   Port 8082      │ │  Port 8081   │ │   Port 8083      │
│                  │ │              │ │                  │
│ • Register       │ │ • CRUD Books │ │ • Purchase       │
│ • Login          │ │ • Inventory  │ │ • Order History  │
│ • JWT Auth       │ │ • By Seller  │ │ • Sales Tracking │
│                  │ │ • Available  │ │                  │
│ ┌──────────────┐ │ │┌────────────┐│ │ ┌──────────────┐ │
│ │  H2 Database │ │ ││ H2 Database││ │ │ H2 Database  │ │
│ │   (userdb)   │ │ ││  (bookdb)  ││ │ │  (orderdb)   │ │
│ └──────────────┘ │ │└────────────┘│ │ └──────────────┘ │
└──────────────────┘ └──────────────┘ └──────────────────┘
             │              │              │
             │              │              │ WebClient
             │              │              └──────┐
             │              │                     │
             └──────────────┴─────────────────────┘
                             │
                             ▼
                  ┌─────────────────────┐
                  │   EUREKA SERVER     │
                  │     Port 8761       │
                  │                     │
                  │  Service Discovery  │
                  │  & Registration     │
                  └─────────────────────┘
```

## Request Flow Examples

### 1. User Registration Flow
```
┌─────────┐     ┌─────────┐     ┌──────────┐     ┌──────────┐
│ Browser │────▶│ Gateway │────▶│   User   │────▶│    H2    │
│         │     │  :8080  │     │ Service  │     │ Database │
│         │     │         │     │  :8082   │     │          │
└─────────┘     └─────────┘     └──────────┘     └──────────┘
    │                                 │
    │◀────────────────────────────────┘
    │         JWT Token
```

### 2. Seller Lists a Book
```
┌─────────┐     ┌─────────┐     ┌──────────┐     ┌──────────┐
│ Seller  │────▶│ Gateway │────▶│   Book   │────▶│    H2    │
│Dashboard│     │  :8080  │     │ Service  │     │ Database │
│         │     │         │     │  :8081   │     │          │
└─────────┘     └─────────┘     └──────────┘     └──────────┘
    │                                 │
    │◀────────────────────────────────┘
    │      Book Created (with sellerId)
```

### 3. Buyer Purchases a Book
```
┌─────────┐     ┌─────────┐     ┌──────────┐     ┌──────────┐     ┌──────────┐
│  Buyer  │────▶│ Gateway │────▶│  Order   │────▶│   Book   │────▶│    H2    │
│Dashboard│     │  :8080  │     │ Service  │     │ Service  │     │ Database │
│         │     │         │     │  :8083   │     │  :8081   │     │          │
└─────────┘     └─────────┘     └──────────┘     └──────────┘     └──────────┘
    │                                 │                 │
    │                                 │◀────────────────┘
    │                                 │  Check Stock & Update
    │                                 │
    │                                 ▼
    │                           ┌──────────┐
    │                           │    H2    │
    │                           │ Database │
    │                           │ (orders) │
    │                           └──────────┘
    │                                 │
    │◀────────────────────────────────┘
    │         Order Confirmation
```

## Database Schema Relationships

```
┌─────────────────┐
│     USERS       │
├─────────────────┤
│ id (PK)         │◀──────────┐
│ name            │           │
│ email           │           │ sellerId (FK)
│ password        │           │
│ role            │           │
└─────────────────┘           │
        │                     │
        │ buyerId (FK)        │
        │                     │
        ▼                     │
┌─────────────────┐     ┌─────────────────┐
│     ORDERS      │     │      BOOKS      │
├─────────────────┤     ├─────────────────┤
│ id (PK)         │     │ id (PK)         │
│ buyer_id (FK)   │     │ title           │
│ book_id (FK)────┼────▶│ author          │
│ seller_id (FK)  │     │ isbn            │
│ quantity        │     │ price           │
│ total_price     │     │ quantity        │
│ status          │     │ seller_id (FK)──┘
│ order_date      │     │ status          │
└─────────────────┘     └─────────────────┘
```

## Service Communication Pattern

```
Order Service needs to purchase a book:

1. Receive purchase request
   ↓
2. Call Book Service via WebClient
   GET /books/{id}
   ↓
3. Validate stock availability
   ↓
4. Update book quantity
   PUT /books/{id}/quantity
   ↓
5. Create order record
   ↓
6. Return order confirmation
```

## Technology Stack Layers

```
┌─────────────────────────────────────────────┐
│           Presentation Layer                │
│              React Frontend                 │
└─────────────────────────────────────────────┘
                    ▼
┌─────────────────────────────────────────────┐
│            API Gateway Layer                │
│         Spring Cloud Gateway                │
└─────────────────────────────────────────────┘
                    ▼
┌─────────────────────────────────────────────┐
│          Business Logic Layer               │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐   │
│  │   User   │ │   Book   │ │  Order   │   │
│  │ Service  │ │ Service  │ │ Service  │   │
│  └──────────┘ └──────────┘ └──────────┘   │
└─────────────────────────────────────────────┘
                    ▼
┌─────────────────────────────────────────────┐
│          Data Persistence Layer             │
│         Spring Data JPA + H2                │
└─────────────────────────────────────────────┘
                    ▼
┌─────────────────────────────────────────────┐
│         Service Discovery Layer             │
│            Netflix Eureka                   │
└─────────────────────────────────────────────┘
```

## Deployment View

```
Development Environment:

┌─────────────────────────────────────────────────────┐
│              localhost (Your Machine)               │
│                                                     │
│  Port 8761: Eureka Server                          │
│  Port 8080: API Gateway                            │
│  Port 8081: Book Service                           │
│  Port 8082: User Service                           │
│  Port 8083: Order Service                          │
│  Port 3000: React Frontend                         │
│                                                     │
│  All services communicate via localhost            │
│  All databases are in-memory (H2)                  │
└─────────────────────────────────────────────────────┘
```

## Security Flow

```
1. User Login
   ↓
2. User Service validates credentials
   ↓
3. Generate JWT Token
   ↓
4. Return token to frontend
   ↓
5. Frontend stores in localStorage
   ↓
6. All subsequent requests include token
   ↓
7. Services can validate token via User Service
```
