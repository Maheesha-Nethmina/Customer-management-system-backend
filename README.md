# 📋 Customer Management System (CMS)

Customer Management System (CMS) is a high-performance, full-stack web application designed for enterprise-level customer data management. It enables users to efficiently manage customer profiles, handle large-scale data ingestion, and ensure fast, optimized data retrieval through a clean and responsive user interface.

---

## 🔗 Repository Links
* **Backend Repository:**  https://github.com/Maheesha-Nethmina/Customer-management-system-backend
* **Frontend Repository:** https://github.com/Maheesha-Nethmina/Customer-management-system-frontend


## ✨ Features

### ✅ Core Functionalities
- **Add / Edit Customers** – Create and update customer profiles with required fields such as Name, Date of Birth, and NIC, with support for dynamic fields  
- **Family Member Linking** – Link existing customers as family members using relational mapping  
- **Bulk Excel Upload** – Upload large datasets (up to **1,000,000 records**) using memory-efficient batch processing  
- **Server-Side Pagination** – Displays **50 records per page** with sorting based on latest entries  

### ⚙️ Technical Highlights
- **Responsive UI** – Built with Tailwind CSS  
- **Error Handling** – Clear validation and error messages  
- **Data Integrity** – Strong backend validation and mapping  
- **High Performance** – Optimized for large-scale data processing  

---

## 🛠️ Tech Stack

**Backend**
- Spring Boot  
- Maven  
- MariaDB  
- JPA / Hibernate  
- Apache POI  

**Frontend**
- React  
- Vite  
- Tailwind CSS  
- Axios  

---

## 🚀 Getting Started

### 📌 Prerequisites
- Java JDK 17+  
- Node.js (v18+)  
- MariaDB  

---

## ⚡ Setup Instructions

### 1. Clone the Front-end Repository 
```bash
git clone https://github.com/Maheesha-Nethmina/Customer-management-system-backend
cd Customer-Management-System
```

### 2. Clone the Back-end Repository 
```bash
git clone https://github.com/Maheesha-Nethmina/Customer-management-system-frontend
cd Customer-Management-System
```

### 3. DataBase SetUp
```bash
CREATE DATABASE  customer_management_system;
```

Update backend configuration in:
```bash
backend/src/main/resources/application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/customer_management_system
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Run Backend Server
```bash
cd backend
mvn spring-boot:run
```

4. Run Frontend
```bash
cd frontend
npm install
npm run dev
```
