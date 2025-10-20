pipeline {
    agent any
    environment {
        DOCKER_REGISTRY = "docker.io"
        DOCKER_ORG = "imshubhamkaushik" // your dockerhub username
        USER_SERVICE_IMAGE = "${DOCKER_ORG}/user-service:latest"
        PRODUCT_SERVICE_IMAGE = "${DOCKER_ORG}/product-service:latest"
        FRONTEND_SERVICE_IMAGE = "${DOCKER_ORG}/frontend-service:latest"
        HELM_CHART_DIR = "helm/shopease-chart" // Directory for Helm chart, can use different directory
        SONARQUBE = "sonarqube" // Jenkins credential ID for SonarQube, can use different Id as per your choice
        DOCKER_CREDENTIALS = "dockerhub" // Jenkins credential ID for dockerhub, can use different Id as per your choice
        KUBERNETES_CREDENTIALS = "kubernetes-config" // Jenkins credential ID for kubernetes, can use different Id as per your choice
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
        stage('Build "Product Service" and SonarQube Analysis') {
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
                    helm upgrade --install shopease helm/shopease-chart \
                    --set userService.image.repository=imshubhamkaushik/user-service \
                    --set productService.image.repository=imshubhamkaushik/product-service \
                    --set frontend.image.repository=imshubhamkaushik/frontend-service \
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
