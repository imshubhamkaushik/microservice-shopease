pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = "docker.io"
        DOCKER_ORG = "imshubhamkaushik" // your dockerhub username
        USER_SERVICE_IMAGE = "${DOCKER_ORG}/user-service:latest"
        PRODUCT_SERVICE_IMAGE = "${DOCKER_ORG}/product-service:latest"
        FRONTEND_SERVICE_IMAGE = "${DOCKER_ORG}/frontend-service:latest"
        
        HELM_CHART_DIR = "helm/shopease-hc" // Directory for Helm chart, can use different directory
        
        SONARQUBE = "sonarqube" // Jenkins credential ID for SonarQube, can use different Id as per your choice
        DOCKER_CREDENTIALS = "dockerhub" // Jenkins credential ID for dockerhub, can use different Id as per your choice
        KUBERNETES_CREDENTIALS = "kubernetes-config" // Jenkins credential ID for kubernetes, can use different Id as per your choice
        DB_PASSWORD_CREDENTIAL_ID = "shopease-db-password" // Jenkins credential ID for DB password, can use different Id as per your choice
        K8S_NAMESPACE = "default"

    }

    stages {

        // Checkout Source Code
        stage('Git Checkout') { 
            steps {
                git branch: 'main', url: 'https://github.com/imshubhamkaushik/microservice-shopease.git'
            }
        }

        // Unit Tests for Backend Services (parallel execution)
        stage('Unit Tests for Backend Services') {
            parallel {
                userService: {
                    stage('"User Service" Unit Tests') {
                        steps {
                            dir('user-service') {
                                bat 'mvn -B test -DskipTests=false '
                            }
                        }
                    }
                }
                productService: {
                    stage('"Product Service" Unit Tests') {
                        steps {
                            dir('product-service') {
                                bat 'mvn -B test -DskipTests=false '
                            }
                        }
                    }
                }
            }
        }

        // User Service Backend Build, SonarQube Analysis, and Quality Gate
        stage('Build "User Service", SonarQube Analysis and Quality Gate') {
            steps {
                dir('user-service') {
                    bat 'mvn -B -DskipTests clean package'
                    withSonarQubeEnv("$SONARQUBE") {
                        bat 'mvn sonar:sonar'
                    }   
                }
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // Product Service Backend Build, SonarQube Analysis, and Quality Gate
        stage('Build "Product Service" and SonarQube Analysis') {
            steps {
                dir('product-service') {
                    bat 'mvn -B -DskipTests clean package'
                    withSonarQubeEnv("$SONARQUBE") {
                        bat 'mvn sonar:sonar'
                    }
                }
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // Frontend Service Build
        stage('Build "Frontend Service"') {
            steps {
                dir('frontend') {
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }

        // Docker Image Build for All Services
        stage ('Build Docker Images for All Services') {
            steps {
                bat "docker build -t ${USER_SERVICE_IMAGE} ./user-service"
                bat "docker build -t ${PRODUCT_SERVICE_IMAGE} ./product-service"
                bat "docker build -t ${FRONTEND_SERVICE_IMAGE} ./frontend"
            }
        }
        
        // Trivy Scan Docker Images
        stage('Trivy Scan Docker Images') {
            steps {
                bat "trivy image --severity HIGH,CRITICAL ${USER_SERVICE_IMAGE}"
                bat "trivy image --severity HIGH,CRITICAL ${PRODUCT_SERVICE_IMAGE}"
                bat "trivy image --severity HIGH,CRITICAL ${FRONTEND_SERVICE_IMAGE}"
            }
        }

        // Push Docker Images to Registry
        stage('Push Docker Images to Registry') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS}", usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    bat "echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin"
                    bat "docker push ${USER_SERVICE_IMAGE}"
                    bat "docker push ${PRODUCT_SERVICE_IMAGE}"
                    bat "docker push ${FRONTEND_SERVICE_IMAGE}"
                }
            }
        }

        //Create K8s secret from Jenkins credential
        stage('Create K8s Secret for DB Password') {
            steps {
                withCredentials([string(credentialsId: "${DB_PASSWORD_CREDENTIAL_ID}", variable: 'DB_PASSWORD')]) {
                    withKubeConfig(credentialsId: "${KUBERNETES_CREDENTIALS}") {
                        // Use kubectl to create/update secret in the specified namespace
                        bat "kubectl create secret generic shopease-secrets --from-literal=DB_PASSWORD=%DB_PASSWORD% --namespace=${K8S_NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -"
                    }
                }
            }
        }

        // Deploy to Kubernetes Using Helm
        stage('Deploy to Kubernetes Using Helm') {
            steps {
                withKubeConfig(credentialsId: "${KUBERNETES_CREDENTIALS}") {
                    bat "helm upgrade --install shopease helm/shopease-hc"
                }
            }
        }
    }

    // Post Actions
    post {
        success { echo 'Pipeline successful!!!' }
        failure { echo 'Pipeline Failed!!! Check Jenkins logs!!!' }
    }
}
