# Bookify - Hotel Reservation System
![Bookify](./ui/public/favicon/favicon.png)
## Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Backend (`server`)](#backend-server)
- [Frontend (`ui`)](#frontend-ui)
- [Environment Variables](#environment-variables)
- [Running the Application](#running-the-application)
- [Customization](#customization)
- [Conclusion](#conclusion)


## Overview

Bookify is a demo project that simulates a basic hotel room booking system.
The project is composed of a monolithic backend service and a frontend application, 
both of which are containerized using Docker.
This setup provides a hands-on demonstration of modern application architecture, 
integration with various databases, and orchestration using Docker Compose.

## Project Structure
```
bookify/ 
├── docker/ 
│ ├── docker-compose.yml 
│ └── .env 
├── server/ 
│ └── Dockerfile 
├── ui/ 
│ ├── Dockerfile 
│ ├── .env.production 
│ └── nginx.conf
```

## Backend (`server`)

The backend service is a Spring Boot Rest application that handles hotel booking logic. 
It interacts with PostgreSQL, MongoDB, Redis, and Kafka for data persistence, statistics tracking, 
caching, and event-driven communication, respectively. 
The backend exposes a REST API documented with OpenAPI.

**Technologies:**
- ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=spring-boot&logoColor=white)
- ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white)
- ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?logo=postgresql&logoColor=white)
- ![MongoDB](https://img.shields.io/badge/MongoDB-47A248?logo=mongodb&logoColor=white)
- ![Redis](https://img.shields.io/badge/Redis-DC382D?logo=redis&logoColor=white)
- ![Kafka](https://img.shields.io/badge/Apache%20Kafka-231F20?logo=apache-kafka&logoColor=white)

## Frontend (`ui`)

The frontend application is built with React and Vite. It provides a user CMS interface for managers to interact with 
the booking system. The frontend is served using Nginx and communicates with the backend via REST API.

**Technologies:**
- ![React](https://img.shields.io/badge/React-61DAFB?logo=react&logoColor=blue)
- ![Vite](https://img.shields.io/badge/Vite-646CFF?logo=vite&logoColor=white)
- ![TanStack](https://img.shields.io/badge/TanStack-FF4154?style=for-the-badge&logo=TanStack&logoColor=white)
- ![Node.js](https://img.shields.io/badge/Node.js-339933?logo=node.js&logoColor=white)
- ![Nginx](https://img.shields.io/badge/Nginx-009639?logo=nginx&logoColor=white)

## Environment Variables

The project uses a `.env` file located in the `docker` directory to manage environment-specific configurations. 
The following variables are defined:

```dotenv
HOST=localhost          # Hostname for accessing the application (change for network access)
SERVER_PORT=8085        # External port for backend service
UI_PORT=8080            # External port for frontend service
```

**Important Notes:**
- If HOST is modified, you must rebuild the server and ui Docker images.
- Changes to SERVER_PORT and UI_PORT require rebuilding the ui image.

## Running the Application

To start the entire application, navigate to the docker directory and run:
```bash
docker compose up --build -d
```
This will build and start all services, including PostgreSQL, MongoDB, Redis, Zookeeper, Kafka, 
the backend server, and the frontend UI.

**Accessing the Application:**
- Frontend (UI): http://localhost:8080
- Backend (API): http://localhost:8085/api

*The application comes with a default administrator account, which you can use to log in:*

**Default Admin Credentials:**
- **name**: `admin`
- **password**: `admin`

**Health Checks:**
- Backend: http://localhost:8085/health

## Customization
To customize the environment or network configuration:

1. Update the .env file in the docker directory.

2. Rebuild the affected Docker images and run using `docker compose up -d`  

## Conclusion

This project serves as a demonstration of integrating multiple technologies into a cohesive application using Docker. 
It showcases the interaction between various services and provides a foundation for building more complex systems.
