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
