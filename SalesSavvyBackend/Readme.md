# SalesSavvy Backend

SalesSavvy Backend is a full-featured RESTful service for an e-commerce platform. It supports **admin operations**, user management, product and category management, order processing, payments, and authentication using **JWT tokens**.

---

## About the Project

SalesSavvy is an e-commerce management system that allows businesses to manage inventory, orders, customers, and payments efficiently.

**How it Works:**

1. Users register and login. JWT tokens are generated upon login to authenticate and authorize requests.
2. Customers can browse products, filter by categories, add products to their cart, and place orders.
3. Orders are processed and payments are handled via Razorpay in test mode.
4. Admins can manage products, categories, users, orders, and generate detailed business reports (daily, monthly, yearly, overall).
5. All actions are secured via JWT-based authentication and role-based access control.

**Admin Features:**

* Manage products, categories, users, orders, and product images
* Generate business reports (daily, monthly, yearly, overall)
* Filter and search orders, products, and users
* Role-based access control with JWT authentication

**Customer Features:**

* Browse products with optional category filters
* Add, update, or remove items in cart
* Place orders and view order history
* Payments via Razorpay (test mode)
* JWT token authentication for secure endpoints
* View profile information

**Security & Auth:**

* JWT tokens are issued at login and stored in HttpOnly cookies
* Each request checks the token to verify user identity
* Role-based access ensures customers cannot access admin endpoints

---

## Tech Stack

* **Java 17**
* **Spring Boot**
* **MySQL 8.0**
* **Razorpay (Test Mode)**
* **JWT Tokens** for authentication
* **Docker & Docker Compose**

---

## Project Structure

```
sales-savvy-backend/
│-- src/               # Application source code
│-- config/            # Configuration files
│-- Dockerfile         # Backend Dockerfile
│-- docker-compose.yml # Runs backend + MySQL
│-- pom.xml            # Maven dependencies
│-- README.md          # Project documentation
```

---

## Dockerization

SalesSavvy backend is fully dockerized for easy deployment.

**Dockerfile:**

* Builds a Java Spring Boot application image
* Copies the jar and configuration files
* Exposes port 9090

**docker-compose.yml:**

* Runs backend service and MySQL together
* Sets environment variables for MySQL:

  ```yaml
  environment:
    MYSQL_ROOT_PASSWORD: SQL Password
    MYSQL_DATABASE: Ecommerce
  ```
* Maps MySQL port `3306` and backend port `9090`
* Allows easy startup with `docker-compose up --build`

**Commands:**

```bash
docker-compose up --build   # Build and start containers
docker-compose down         # Stop and remove containers
```

---

## Setup & Installation

1. Clone the repository:

```bash
git clone https://github.com/your-username/sales-savvy-backend.git
cd sales-savvy-backend
```

2. Build and run with Docker:

```bash
docker-compose up --build
```

* Backend API: `http://localhost:9090`
* MySQL: `localhost:3306` (username: `root`, password: `8277006863`)

---

## Database

* Schema: `Ecommerce`
* Tables: `products`, `categories`, `users`, `orders`, `orderitems`, `productimages`, `jwt_tokens`
* JDBC URL: `jdbc:mysql://localhost:3306/Ecommerce`

---

## Running Without Docker

```bash
mysql -u root -p
mvn spring-boot:run
```

---

## API Endpoints

### Admin Endpoints

| Method | Endpoint                | Description             |
| ------ | ----------------------- | ----------------------- |
| GET    | /admin/business/daily   | Daily business report   |
| GET    | /admin/business/monthly | Monthly business report |
| GET    | /admin/business/yearly  | Yearly business report  |
| GET    | /admin/business/overall | Overall business report |
| POST   | /admin/products/add     | Add a product           |
| DELETE | /admin/products/delete  | Delete a product        |
| GET    | /admin/users            | Get all users           |
| PUT    | /admin/user/modify      | Modify user             |
| POST   | /admin/user/getbyid     | Get user by ID          |

### Customer Endpoints

| Method | Endpoint              | Description                                   |
| ------ | --------------------- | --------------------------------------------- |
| POST   | /api/users/register   | Register a new user                           |
| POST   | /api/auth/login       | Login user, receive JWT token in cookie       |
| POST   | /api/auth/logout      | Logout user, clear JWT cookie                 |
| GET    | /api/products         | Browse products with optional category filter |
| POST   | /api/cart/add         | Add item to cart                              |
| PUT    | /api/cart/update      | Update item quantity in cart                  |
| DELETE | /api/cart/delete      | Remove item from cart                         |
| GET    | /api/cart/items       | Get cart items                                |
| GET    | /api/cart/items/count | Get total number of cart items                |
| POST   | /api/orders           | Place an order                                |
| GET    | /api/orders           | Get orders for authenticated user             |
| POST   | /api/payment/create   | Create Razorpay order                         |
| POST   | /api/payment/verify   | Verify Razorpay payment                       |
| GET    | /api/profile/info     | Get profile info for authenticated user       |

---

## License

MIT License
