# ShopEase Microservices (Spring Boot) — Quick Start

## Prerequisites
- Docker & Docker Compose
- Maven (optional, only if you want to build locally)
- Java 17 (optional)

## Start with Docker Compose
From project root:
```bash
docker-compose up --build

Services:

User service: http://localhost:8081

GET /users - list users
POST /users/register - register
POST /users/login - login

Product service: http://localhost:8082

GET /products - list products
POST /products - create product

Build and run locally (without Docker)

Each service is a standard Spring Boot app. 

From user-service/:

mvn clean package
java -jar target/user-service-0.0.1-SNAPSHOT.jar

From product-service/:

mvn clean package
java -jar target/product-service-0.0.1-SNAPSHOT.jar

Notes

DB: PostgreSQL at jdbc:postgresql://localhost:5432/shopease (when running compose)

This is a demo; authentication is basic and not secure.


---

# 5) Example `Jenkinsfile` (declarative pipeline)
Put at project root — **example** showing how you might build both services and create Docker images. You’ll need Jenkins with Docker build/publish rights and credentials for DockerHub (or private registry).

```groovy
pipeline {
  agent any
  environment {
    DOCKER_REGISTRY = "docker.io"
    DOCKER_ORG = "your-dockerhub-username"
  }
  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build user-service') {
      steps {
        dir('user-service') {
          sh 'mvn -B -DskipTests clean package'
        }
      }
    }

    stage('Build product-service') {
      steps {
        dir('product-service') {
          sh 'mvn -B -DskipTests clean package'
        }
      }
    }

    stage('Docker Build & Push') {
      steps {
        script {
          sh "docker build -t ${DOCKER_ORG}/user-service:latest user-service"
          sh "docker build -t ${DOCKER_ORG}/product-service:latest product-service"
          // login step - assumes credentials configured in Jenkins
          withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
            sh "echo $PASS | docker login -u $USER --password-stdin ${DOCKER_REGISTRY}"
            sh "docker push ${DOCKER_ORG}/user-service:latest"
            sh "docker push ${DOCKER_ORG}/product-service:latest"
          }
        }
      }
    }

    stage('Deploy (optional)') {
      steps {
        echo 'You can add deploy steps here (kubectl apply / helm upgrade / ssh ansible...)'
      }
    }
  }
}

7) Quick curl examples to test after docker-compose up
# list users
curl http://localhost:8081/users

# register
curl -X POST http://localhost:8081/users/register -H "Content-Type: application/json" -d '{"name":"Charlie","email":"charlie@example.com","password":"charlie123"}'

# login
curl -X POST http://localhost:8081/users/login -H "Content-Type: application/json" -d '{"email":"alice@example.com","password":"alice123"}'

# list products
curl http://localhost:8082/products

# create product
curl -X POST http://localhost:8082/products -H "Content-Type: application/json" -d '{"name":"Hat","description":"A cool hat","price":199.0}'

# FRESH

🚀 Architecture Overview

                        +----------------------+
                        |      Frontend        |
                        |  React + Nginx (FE)  |
                        +----------+-----------+
                                   |
                     Ingress / Load Balancer (K8s)
                                   |
          -------------------------------------------------
          |                                               |
+---------v--------+                             +--------v---------+
|   User Service   |                             | Product Service |
| Spring Boot (8081)|                             | Spring Boot (8082)|
| DTOs, Validation |                             |  CRUD Products   |
| BCrypt Passwords |                             |                  |
+---------+--------+                             +---------+--------+
          |                                               |
          -----------------------+ +-----------------------
                                  |
                         +--------v--------+
                         |   PostgreSQL    |
                         |   16-alpine     |
                         +-----------------+


📦 Features Implemented
✔ Frontend

React SPA

User Management

Product Management

API communication via .env

Nginx reverse proxy through Docker

✔ Backend (Microservices)

User Service

Register, Login, List Users

Password hashing with BCrypt

Validation (DTO level)

Global Exception Handler

Rate Limiting (IP-based)

Unit + controller tests

Product Service

CRUD Products

DTOs + Validation

Integration tests

Sonar-ready

✔ Database

PostgreSQL 16-alpine

Unique constraint on email

Optional Seed Data

✔ DevOps Additions

Multi-stage Dockerfiles

Docker Compose

Actuator health endpoints

Prometheus metrics

Kubernetes deployments with:

Liveness Probe

Readiness Probe

Custom Service Accounts

Disabled automount tokens

Resource requests & limits

Helm chart for all services

SonarQube integration

Jacoco coverage reporting

Ready for CI/CD (Jenkins / GitHub Actions)


🐳 Local Development (Docker Compose)

Build & run all services locally:

docker-compose up --build


Services:

Service	URL
Frontend	http://localhost:3000

User Service	http://localhost:8081

Product Service	http://localhost:8082

PostgreSQL	localhost:5432


Stop the stack:

docker-compose down



🧪 Running Tests
User service tests
mvn -f user-service clean test

Product service tests
mvn -f product-service clean test

Coverage Report (Jacoco)
mvn -f user-service jacoco:report
mvn -f product-service jacoco:report


Open HTML report:

user-service/target/site/jacoco/index.html
product-service/target/site/jacoco/index.html


🔍 SonarQube Analysis

Run SonarQube scan:

mvn -f user-service sonar:sonar \
  -Dsonar.host.url=http://SONAR_URL:9000 \
  -Dsonar.login=TOKEN


☸ Kubernetes Deployment (Helm)
Install / Upgrade the chart:
helm upgrade --install shopease helm/shopease-chart -n shopease

Validate templates:
helm template shopease helm/shopease-chart > rendered.yaml
kubectl apply -f rendered.yaml --dry-run=client

Check pod status
kubectl get pods -n shopease
kubectl describe pod <pod> -n shopease

Forward ports (for local debugging)
kubectl port-forward svc/user-service 8081:8081 -n shopease
kubectl port-forward svc/product-service 8082:8082 -n shopease


💠 Helm Chart Components

Includes:

Deployments with:

serviceAccountName

automountServiceAccountToken: false

resources (req/limits)

readiness + liveness probes

Services

Postgres StatefulSet / PVC

Ingress (optional)




🔐 Security Features

DB credentials injected via environment variables

Password hashing (BCrypt)

Sensitive endpoints hidden

No service account tokens mounted

RBAC minimized

Actuator liveness/readiness only




📁 Project Structure
microservice-shopease/
│
├── frontend/               # React + Nginx build
├── user-service/           # Spring Boot user microservice
│   ├── src/main/java
│   ├── src/test/java
│   ├── Dockerfile
│   └── pom.xml
│
├── product-service/        # Spring Boot product microservice
│   ├── src/main/java
│   ├── src/test/java
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yml
│
└── helm/shopease-chart/    # Kubernetes Helm chart
    ├── templates/
    ├── values.yaml
    └── Chart.yaml

📈 Future Enhancements

Planned next steps:

CI/CD pipeline with Jenkins + Sonar + Trivy + Helm deploy

EKS deployment using Terraform

Secrets Manager / External Secrets

Horizontal Pod Autoscaling

Logging stack (ELK or Loki)

Service Mesh (Istio / Linkerd)






# 🛒 Shopease – Microservices E-Commerce Platform with Full DevOps CI/CD

Shopease is a full-stack **microservices-based e-commerce platform** built using **Spring Boot, React, PostgreSQL** and deployed using a **complete DevOps toolchain** including **Docker, Jenkins, Helm, Kubernetes, Trivy, SonarQube, and GitHub Actions**.

This project demonstrates **real-world DevOps practices** such as:
- CI/CD automation
- Containerization
- Security scanning
- Infrastructure deployment
- Kubernetes orchestration
- Helm-based application packaging

---

## 🚀 Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL

### Frontend
- React
- NGINX

### DevOps & Cloud
- Docker & Docker Compose
- Jenkins (CI/CD)
- Kubernetes
- Helm
- Trivy (Security Scanning)
- SonarQube (Code Quality)
- GitHub Actions (CI placeholder)
- Bash Scripting

---

## 📦 Microservices Architecture

| Service | Description |
|--------|-------------|
| User Service | User Registration, Login, Password Encryption |
| Product Service | Product Management APIs |
| Frontend | React UI served through NGINX |
| PostgreSQL | Centralized database |

---

## 🏗 Project Structure

microservice-shopease/
│
├── frontend/ # React UI + NGINX
│
├── user-service/ # Spring Boot User Microservice
├── product-service/ # Spring Boot Product Microservice
│
├── helm/ # Helm charts for Kubernetes
├── ci/ # Trivy security scans
│
├── docker-compose.yml # Local multi-container setup
├── Jenkinsfile # Full CI/CD pipeline
├── .github/workflows/ # GitHub Actions CI workflow (WIP)
└── README.md

yaml
Copy code

---

## 🔄 CI/CD Pipeline (Jenkins)

The Jenkins pipeline performs:

1. **Code Checkout**
2. **Maven Build & Unit Testing**
3. **SonarQube Code Quality Scan**
4. **Docker Image Build**
5. **Trivy Security Scan**
6. **Docker Image Push to DockerHub**
7. **HelM-Based Kubernetes Deployment**

All credentials are securely managed via **Jenkins Credentials**:
- DockerHub
- SonarQube
- Kubernetes Config
- Database Secrets

---

## 🐳 Running Project Locally (Docker Compose)

### 1️⃣ Set Environment Variables

Create `.env` file:

POSTGRES_USERNAME=shopease
POSTGRES_PASSWORD=shopease
POSTGRES_DB=shopease_db

perl
Copy code

### 2️⃣ Start Services

```bash
docker-compose up --build
3️⃣ Access Applications
Service	URL
Frontend	http://localhost:3000
User API	http://localhost:8081
Product API	http://localhost:8082
PostgreSQL	localhost:5432

☸ Kubernetes Deployment Using Helm
bash
Copy code
helm upgrade --install shopease helm/shopease-hc
Verify:

bash
Copy code
kubectl get pods
kubectl get svc
🔐 Security & Reliability Features
✅ Password encryption using BCrypt

✅ Rate limiting filter

✅ Centralized exception handling

✅ Trivy container vulnerability scanning

✅ Health checks for PostgreSQL

✅ Docker image hardening

🧪 Testing
Each microservice includes:

Unit tests for Controllers

Unit tests for Service layers

Run:

bash
Copy code
mvn test
📊 Code Quality
SonarQube integrated into Jenkins

Static code analysis on every build

✅ Current Project Status
✔ CI/CD Automation
✔ Dockerized Microservices
✔ Helm-based Kubernetes Deployment
✔ Security Scanning
✔ PostgreSQL Integration
✔ Frontend–Backend Connectivity

📌 Future Enhancements
GitHub Actions full CI pipeline

Prometheus + Grafana Monitoring

Centralized Logging (ELK / OpenSearch)

Secrets Management (Vault / K8s Secrets)

Autoscaling & Load Testing