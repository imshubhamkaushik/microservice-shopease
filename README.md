# End-to-End DevSecOps CI/CD Pipeline for Microservices

**(Jenkins · Docker · Kubernetes · Helm · SonarQube · Trivy)**

---

## Project Overview

This project demonstrates the design and implementation of an end-to-end DevSecOps CI/CD pipeline for a containerized, microservices-based application.

The primary objective is to showcase secure, automated application delivery using modern DevOps and DevSecOps practices, including:

- CI/CD pipeline automation using Jenkins
- Containerization of microservices using Docker
- Static code analysis and quality enforcement using SonarQube
- Container and configuration security scanning using Trivy
- Deployment and orchestration using Kubernetes and Helm

The project intentionally focuses on pipeline design, security integration, and deployment automation, rather than application business logic.

---

## Why This Project?

This project focuses on CI/CD and DevSecOps practices for containerized applications, complementing infrastructure-focused DevOps projects.

Key emphasis areas:

- Secure CI/CD pipeline design using Jenkins
- Shift-left security using automated scanning
- Kubernetes-based application delivery using Helm

Infrastructure provisioning (Terraform / Ansible) is intentionally kept out of scope to maintain clear separation of responsibilities.

---

## Tech Stack

- CI/CD: Jenkins
- Containers: Docker
- Orchestration: Kubernetes
- Package Management: Helm
- Security & Quality: SonarQube, Trivy
- Backend Services: Spring Boot (Microservices)
- Frontend: React
- Container Runtime: Linux

---

## Architecture Overview

### High-Level Architecture

- Microservices are developed using Spring Boot
- Each service is containerized using Docker
- Jenkins orchestrates the CI/CD pipeline
- SonarQube performs static code analysis with quality gates
- Trivy scans container images and Kubernetes manifests
- Helm charts manage Kubernetes deployments
- Kubernetes handles service orchestration, scaling, and health checks

 Architecture diagram will be added in a future update.

---

## CI/CD Pipeline Workflow

The Jenkins pipeline follows a stage-based DevSecOps workflow:

1. Source code checkout from Git
2. Parallel unit testing for backend microservices
3. Static code analysis using SonarQube
4. Quality gate enforcement to prevent insecure builds
5. Docker image build for all services
6. Container image vulnerability scanning using Trivy
7. Push images to container registry
8. Kubernetes secret creation using Jenkins credentials
9. Security scanning of Helm/Kubernetes manifests using Trivy
10. Deployment to Kubernetes using Helm

This workflow ensures secure, repeatable, and automated deployments.

---

## Repository Structure
microservice-shopease/
├── frontend/
│   └── src/
|   └── .dockerignore
|   └── Dockerfile
|   └── nginx.conf
|   └── package.json
│
├── user-service/
|   └── src/
|   └── .dockerignore
|   └── Dockerfile
|   └── pom.xml
|   └── sonar-project.properties
|
├── product-service/
|   └── src/
|   └── .dockerignore
|   └── Dockerfile
|   └── pom.xml
|   └── sonar-project.properties
│
├── helm/
│   └── shopease-hc/
│       ├── templates/
│       └── values.yaml
|       └── Chart.yaml
│
├── .trivyignore
├── docker-compose.yaml
├── Jenkinsfile
├── .gitignore
└── README.md

---

## Containerization (Docker)

- Each microservice uses a multi-stage Dockerfile
- Build and runtime stages are separated
- Lightweight runtime images are used to reduce attack surface

This improves security, portability, and deployment consistency.

---

## Kubernetes & Helm Deployment

### Kubernetes

- Services are deployed as Kubernetes Deployments
- Health probes ensure application reliability
- Resource requests and limits enforce controlled resource usage
- StatefulSet with persistent storage is used for PostgreSQL

### Helm

- Helm charts manage Kubernetes manifests
- Values files enable environment-specific configuration
- Helm enables versioned and repeatable deployments

This setup mirrors real-world Kubernetes deployment patterns.

---

## DevSecOps Integration

### Static Code Analysis (SonarQube)

- Jenkins integrates SonarQube for code quality checks
- Quality gates enforce minimum standards before deployment

### Container & Configuration Security (Trivy)

- Docker images are scanned for HIGH and CRITICAL vulnerabilities
- Helm and Kubernetes manifests are scanned for misconfigurations
- Pipeline execution fails on critical security findings

Security is treated as a first-class citizen in the CI/CD workflow.

---

## How to Run the Project

### Prerequisites

- Docker
- Kubernetes cluster (local or managed)
- Helm
- Jenkins
- SonarQube
- Trivy

### High-Level Execution Flow

1. Configure Jenkins with required credentials
2. Push code changes to the Git repository
3. Jenkins pipeline triggers automatically
4. Services are built, scanned, and deployed to Kubernetes

  Detailed environment-specific setup steps are intentionally abstracted to keep the focus on CI/CD pipeline design and DevSecOps concepts.

---

## Current Limitations

- Designed and tested in a limited environment
- No external traffic or load testing
- Observability limited to Kubernetes health checks
- Focus remains on CI/CD and security automation

These constraints are intentional to keep the project focused and explainable.

---

## Key Learnings

- Designing secure CI/CD pipelines using Jenkins
- Integrating security scanning into build pipelines
- Containerizing microservices using Docker
- Managing Kubernetes deployments using Helm
- Applying DevSecOps principles in real-world workflows