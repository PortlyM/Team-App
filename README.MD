# Team App

A full-stack web application designed for seamless team collaboration, task management, and productivity tracking.

## Overview

**Team App** is a modern solution that helps teams stay organized and focused. It provides a robust backend API built with Java and Spring Boot, and a dynamic, responsive frontend built with Next.js and React.

### Key Features
- **User Authentication & Authorization**: Secure login and registration using JWT (JSON Web Tokens).
- **Team Management**: Create, join, and manage teams efficiently.
- **Task & To-Do Lists**: Organize work with dedicated to-do lists and tasks assigned to users.
- **RESTful API**: A well-structured backend providing a reliable and fast API.
- **Modern UI/UX**: A beautiful, responsive frontend styled with TailwindCSS v4.

---

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.x** (WebMVC, Data JPA, Security)
- **PostgreSQL** (Relational Database)
- **JWT** (Authentication)
- **MapStruct & Lombok** (Boilerplate reduction & object mapping)
- **Maven** (Dependency Management)

### Frontend
- **Next.js 16.2.9**
- **React 19.2**
- **TypeScript**
- **TailwindCSS v4**

### Infrastructure
- **Docker & Docker Compose** (Containerized database setup)

---

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Make sure you have the following installed:
- [Java 17](https://jdk.java.net/17/)
- [Node.js](https://nodejs.org/) (v18 or higher)
- [Docker](https://www.docker.com/) and Docker Compose
- Maven (Optional, as the project includes Maven Wrapper)

### 1. Clone the repository

```bash
git clone <your-repository-url>
cd Team-App
```

### 2. Run the Database (Docker)

The project includes a `docker-compose.yml` file to quickly spin up a PostgreSQL instance.

```bash
cd backend
docker-compose up -d
```
*This will start a PostgreSQL database on port 5432 with the database name `postgres_teamapp`.*

### 3. Start the Backend server

Open a new terminal window, navigate to the `backend` directory, and run the Spring Boot application using Maven Wrapper:

```bash
cd backend
./mvnw spring-boot:run
```
*The backend server will start on `http://localhost:8080` (default).*

### 4. Start the Frontend client

Open another terminal window, navigate to the `frontend` directory, install dependencies, and start the development server:

```bash
cd frontend
npm install
npm run dev
```
*The Next.js frontend will be available at `http://localhost:3000`.*

---

## Project Structure

```text
Team App/
├── backend/                  # Spring Boot API
│   ├── src/main/java/...     # Java Source Code (Controllers, Entities, Repositories)
│   ├── src/main/resources/   # Application properties
│   ├── docker-compose.yml    # Database container configuration
│   └── pom.xml               # Maven dependencies
├── frontend/                 # Next.js Application
│   ├── app/                  # Next.js App Router pages
│   ├── components/           # Reusable React components
│   ├── package.json          # Node.js dependencies
│   └── tailwind.config.js    # TailwindCSS configuration
└── README.MD                 # Project documentation
```

---

## API Endpoints Overview

The backend exposes several RESTful endpoints. Authentication is required for most of them via a Bearer JWT Token.

- **Auth**: `/api/auth/register`, `/api/auth/login`
- **Users**: `/api/users/*`
- **Teams**: `/api/teams/*`
- **To-Do Lists**: `/api/todolists/*`
- **Tasks**: `/api/tasks/*`

*(Note: Exact paths might vary based on Controller mappings)*

---
