# Inventory Management System

A comprehensive system for managing items, inventory balances, locations, stock movements, suppliers, and replenishment rules.  
This project was created to simplify warehouse control and automate essential operations.

---

## ⭐ Key Features

* Create and edit **items**
* Manage **inventory balances across locations**
* Track **stock movements** between locations
* Configure **reorder rules**
* Manage **suppliers**
* Manage **warehouse locations**
* **User authentication** and role-based access

---

## 🛠️ Technology Stack

* **Backend:** Java (Spring Boot)  
* **Database:** MySQL  
* **Frontend:** HTML / CSS / JavaScript  

---

## 📁 Project Structure

```
/Inventory
│── Inventory/
│── database/
│    └── warehouse_inventory.sql
│── docs/
│    ├── er-diagram
│    │    ├── ClassDiagram.png
│    │    ├── ComponentDiagram.png
│    │    ├── SequenceDiagram-Admin.png
│    │    ├── SequenceDiagram-Loader.png
│    │    ├── SequenceDiagram-Manager.png
│    │    └── UseCaseDiagram.png
│    └── screen-page
│         ├── AdminPage.png
│         ├── AdminUsersPage.png
│         ├── LoaderInventoryPage.png
│         ├── LoaderMovementsPage.png
│         ├── LoaderPage.png
│         ├── LoginPage.png
│         ├── ManagerInventoryPage.png
│         ├── ManagerItemsPage.png
│         ├── ManagerLocationsPage.png
│         ├── ManagerMovementsPage.png
│         ├── ManagerReorderPage.png
│         └── ManagerSuppliersPage.png
│── .gitattributes
│── .gitignore
│── LICENSE
└── README.md
```

---

## 🧩 ER-діаграми

## Use Case Diagrams
![Use Case Diagram](docs/er-diagram/UseCaseDiagram.png)

---

## Class Diagram
![Class Diagram](docs/er-diagram/ClassDiagram.png)

## Component Diagram
![Component Diagram](docs/er-diagram/ComponentDiagram.png)

## Sequence Diagrams

### Admin
![Sequence Diagram - Admin](docs/er-diagram/SequenceDiagram-Admin.png)

### Loader
![Sequence Diagram - Loader](docs/er-diagram/SequenceDiagram-Loader.png)

### Manager
![Sequence Diagram - Manager](docs/er-diagram/SequenceDiagram-Manager.png)

---

## 📦 Installation

### 1. Clone the repository

```
git clone https://github.com/86Undertaker86/Inventory.git
```

### 2. Configure the environment

Edit **application.properties**:

```
spring.datasource.url=jdbc:mysql://localhost:3306/warehouse_inventory
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# === JDBC Driver Configuration ===
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 3. Import the database

1. Open **MySQL Workbench** or **MySQL 8.0 Command Line Client**  
2. Import `warehouse_inventory.sql` from the `/database` folder

---

## ▶️ Running the Application

Start the Spring Boot project:

```
mvn spring-boot:run
```

---

## 📸 Скриншоти

## Login Page
![Login Page](docs/screen-page/LoginPage.png)

## Admin Page
![Admin Page](docs/screen-page/AdminPage.png)

### Admin Users Page
![Admin Users Page](docs/screen-page/AdminUsersPage.png)

## Manager Page
![Manager Page](docs/screen-page/ManagerPage.png)

### Manager Items Page
![Manager Items Page](docs/screen-page/ManagerItemsPage.png)

### Manager Locations Page
![Manager Locations Page](docs/screen-page/ManagerLocationsPage.png)

### Manager Reorder Page
![Manager Reorder Page](docs/screen-page/ManagerReorderPage.png)

### Manager Suppliers Page
![Manager Suppliers Page](docs/screen-page/ManagerSuppliersPage.png)

### Manager Inventory Page
![Manager Inventory Page](docs/screen-page/ManagerInventoryPage.png)

### Manager Movements Page
![Manager Movements Page](docs/screen-page/ManagerMovementsPage.png)

## Loader Page
![Loader Page](docs/screen-page/LoaderPage.png)

### Loader Inventory Page
![Loader Inventory Page](docs/screen-page/LoaderInventoryPage.png)

### Loader Movements Page
![Loader Movements Page](docs/screen-page/LoaderMovementsPage.png)

---

## 📜 License

This project is distributed under the **MIT License**.

---

## 👤 Author

**Danil <86Undertaker86> Sidorchuk** — developer and creator of the project.
