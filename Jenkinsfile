pipeline {
    agent any
    environment {
        DOCKER_REGISTRY = "docker.io"
        DOCKER_ORG = "imshubhamkaushik" // your dockerhub username
        USER_SERVICE_IMAGE = "user-service"
        PRODUCT_SERVICE_IMAGE = "product-service"
        FRONTEND_SERVICE_IMAGE = "frontend-service"
        HELM_CHART_DIR = "helm/shopease-chart"
        SONARQUBE = "sonarqube" // Name from Jenkins Config
        DOCKER_CREDENTIALS = "dockerhub" // Jenkins credential ID for dockerhub
        KUBERNETES_CREDENTIALS = "kubernetes-config" // Jenkins credential ID for kubernetes
    }
    stages {
        // Checkout Source Code
        stage('Git Checkout') { 
            steps {
                git branch: 'main', url: 'https://github.com/imshubhamkaushik/microservice-shopease.git'
            }
        }

        // User Service Backend Build, SonarQube Analysis, and Quality Gate
        stage('Build "User Service" and SonarQube Analysis') {
            steps {
                dir('user-service') {
                    bat 'mvn -B -DskipTests clean package'
                    withSonarQubeEnv("$SONARQUBE") {
                        bat 'mvn sonar:sonar'
                    }
                }
            }
        }

        stage('Quality Gate For User Service') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // Product Service Backend Build, SonarQube Analysis, and Quality Gate
        stage('Build "Product Service"') {
            steps {
                dir('product-service') {
                    bat 'mvn -B -DskipTests clean package'
                    withSonarQubeEnv("$SONARQUBE") {
                        bat 'mvn sonar:sonar'
                    }
                }
            }
        }

        stage('Quality Gate For Product Service') {
            steps {
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

        // Docker Image Build
        stage ('Build Docker Image') {
            steps {
                bat "docker build -t ${USER_SERVICE_IMAGE} ./user-service"
                bat "docker build -t ${PRODUCT_SERVICE_IMAGE} ./product-service"
                bat "docker build -t ${FRONTEND_SERVICE_IMAGE} ./frontend"
                
            }
        }

        stage('Push Docker Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS}", usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    bat "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                    bat "docker push ${USER_SERVICE_IMAGE}"
                    bat "docker push ${PRODUCT_SERVICE_IMAGE}"
                    bat "docker push ${FRONTEND_SERVICE_IMAGE}"
                }
            }
        }

        // Deploy to Kubernetes Using Helm
        stage('Deploy to Kubernetes via Helm') {
            steps {
                withKubeConfig(credentialsId: "${KUBERNETES_CREDENTIALS}") {
                    bat """
                    helm upgrade --install shopease ${HELM_CHART_DIR} ^
                    --set userService.image.repository=${USER_SERVICE_IMAGE} ^
                    --set productService.image.repository=${PRODUCT_SERVICE_IMAGE} ^
                    --set frontend.image.repository=${FRONTEND_SERVICE_IMAGE}
                    """  
                }
                
            }
        }
    }

    // Post Actions
    post {
        success {
            echo 'Build and deployment successful!!!'
        }
        failure {
            echo 'Pipeline Failed!!! Check Jenkins logs!!!'
        }
    }
}
