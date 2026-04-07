# 🍔 QuickBite — Food Ordering Application

> A full-stack food ordering web app with real-time order tracking, live map delivery, JWT authentication, and an admin analytics dashboard — inspired by Zomato/Swiggy.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-brightgreen?style=flat-square&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?style=flat-square&logo=mysql)
![JWT](https://img.shields.io/badge/Auth-JWT-purple?style=flat-square&logo=jsonwebtokens)
![WebSocket](https://img.shields.io/badge/Real--Time-WebSocket%20%2B%20STOMP-red?style=flat-square)
![Leaflet](https://img.shields.io/badge/Maps-Leaflet.js-green?style=flat-square&logo=leaflet)

---

## 📌 Overview

**QuickBite** is a full-stack food ordering application where users can browse a menu, add items to a cart, place orders with multiple payment methods, and track their delivery in real time on a live map. Admins can manage the menu, update order statuses, and view analytics on a dashboard.

---

## ✨ Features

### 👤 User Side
- 🔐 Register & Login with JWT-based authentication
- 🍕 Browse full menu with categories
- 🛒 Cart stored in `localStorage` (no extra DB table)
- 💳 Checkout with Card / UPI / Net Banking / COD
- 📦 Real-time order tracking with live status updates
- 🗺️ Live delivery map using Leaflet.js + OpenStreetMap
- ⏱️ Zomato-style ETA countdown timer
- 📧 Order confirmation email via Gmail SMTP

### 🛠️ Admin Side
- 📋 Manage all orders and update statuses
- 🗂️ Add / Delete menu items
- 📊 Dashboard with Chart.js (Bar + Doughnut charts)
- 🔒 Role-based access — Admin-only routes protected

---

## 🧰 Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| Backend | Java 17 + Spring Boot 3.2.x | REST API, Business Logic |
| Security | Spring Security + JWT (jjwt 0.11.5) | Auth & Role-based Access |
| Database | MySQL 8.x + Spring Data JPA / Hibernate | Persistence |
| Real-Time | WebSocket + STOMP + SockJS | Live order push updates |
| Maps | Leaflet.js 1.9.4 + OpenStreetMap | Free, no API key needed |
| Charts | Chart.js 4.4.1 | Admin analytics |
| Email | Spring Boot Mail (JavaMailSender) | Order notifications |
| Frontend | HTML5 + CSS3 + Vanilla JavaScript | UI (no framework) |
| Build | Maven | Dependency management |
| IDE | Spring Tool Suite (STS) | Development |

---

## ⚡ Real-Time Features

QuickBite qualifies as a **real-time application**. Here's how it works:

```
Admin updates order status
        ↓
PUT /api/admin/orders/{id}/status
        ↓
OrderService.updateStatus() → saves to MySQL
        ↓
SimpMessagingTemplate.convertAndSend("/topic/order/{id}", update)
        ↓
User browser (subscribed via Stomp.js) receives push instantly
        ↓
ETA timer resets + Progress bar updates + Map marker moves + Live feed entry added
```

---

## 🗄️ Database Schema (Entity Relationships)

```
User (1) ──────────── (N) Order
                            │
                           (1)
                            │
                           (N)
                        OrderItem (N) ──── (1) MenuItem
```

- `User` → `Order` : **One-to-Many**
- `Order` → `OrderItem` : **One-to-Many**
- `OrderItem` → `MenuItem` : **Many-to-One**

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- MySQL 8.x
- Maven 3.x

### 1. Clone the Repository
```bash
git clone https://github.com/Sai-Vijaya-Krishna/QuickBite.git
cd QuickBite
```

### 2. Configure the Database
Create a MySQL database:
```sql
CREATE DATABASE quickbite;
```

Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/quickbite
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update

# JWT Secret
app.jwt.secret=YOUR_SECRET_KEY

# Gmail SMTP
spring.mail.username=YOUR_GMAIL
spring.mail.password=YOUR_APP_PASSWORD
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The app starts at **http://localhost:8080**

> 📌 On first run, `DataSeeder.java` auto-creates the admin account and seeds 14 menu items.

### Default Admin Credentials
```
Email:    admin@quickbite.com
Password: admin123
```

---

## 📡 API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and receive JWT |

### Menu
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/menu` | Get all menu items |

### Orders
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/orders/place` | Place a new order |
| GET | `/api/orders/my` | Get current user's orders |
| GET | `/api/orders/{id}` | Get order by ID |

### Admin (🔒 ADMIN role required)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/admin/orders` | Get all orders |
| PUT | `/api/admin/orders/{id}/status` | Update order status |
| POST | `/api/admin/menu` | Add menu item |
| DELETE | `/api/admin/menu/{id}` | Delete menu item |

### WebSocket
| Topic | Description |
|---|---|
| `/topic/order/{id}` | Subscribe to live order status updates |

---

## 🔐 Security Architecture

```
Request → JwtFilter → validates token → sets SecurityContext
                 ↓
         SecurityConfig rules:
         /api/auth/**     → Public
         /api/admin/**    → ADMIN role only
         /api/**          → Authenticated users
```

- Passwords hashed with **BCryptPasswordEncoder**
- Sessions set to **STATELESS** (no server-side session storage)
- CSRF disabled (REST + JWT architecture)

---

## 📁 Project Structure

```
QuickBite/
├── src/main/java/com/quickbite/
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   └── WebSocketConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── OrderController.java
│   │   └── AdminController.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── OrderService.java
│   │   └── EmailService.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── OrderRepository.java
│   │   └── MenuItemRepository.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   └── MenuItem.java
│   ├── security/
│   │   ├── JwtUtil.java
│   │   └── JwtFilter.java
│   └── DataSeeder.java
├── src/main/resources/
│   ├── static/          ← HTML, CSS, JS frontend files
│   └── application.properties
└── pom.xml
```

---

## 🌱 Known Limitations & Future Improvements

| Area | Current State | Planned Improvement |
|---|---|---|
| Payments | Mock simulation | Integrate Razorpay gateway |
| Delivery tracking | Simulated location | Real GPS via driver mobile app |
| Admin stats | Refresh to update | Auto-polling / WebSocket |
| Testing | None | JUnit 5 + Mockito unit tests |
| Validation | None | `@Valid`, `@NotBlank` annotations |
| Pagination | None | Spring Data pagination on order history |
| Deployment | None | Docker + cloud deployment |
| Frontend | Vanilla JS | Migrate to React |
| JWT | Access token only | Add refresh token mechanism |

---

## 👨‍💻 Author

**Sai Vijaya Krishna**
- 🔗 GitHub: [github.com/Sai-Vijaya-Krishna](https://github.com/Sai-Vijaya-Krishna)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

> *Built as a full-stack learning project demonstrating Spring Boot, JWT, WebSocket, and JPA in a real-world food ordering context.*
