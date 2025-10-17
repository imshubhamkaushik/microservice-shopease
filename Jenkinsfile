pipeline {
    agent any
    environment {
        DOCKER_REGISTRY = "docker.io"
        DOCKER_ORG = "imshubhamkaushik" // your dockerhub username
        USER_SERVICE_IMAGE = "${DOCKER_ORG}/user-service:latest"
        PRODUCT_SERVICE_IMAGE = "${DOCKER_ORG}/product-service:latest"
        FRONTEND_SERVICE_IMAGE = "${DOCKER_ORG}/frontend-service:latest"
        HELM_CHART_DIR = "shopease-chart"
        SONARQUBE = "SonarQube" // Name from Jenkins Config
        DOCKER_CREDENTIALS = credentials('dockerhub') // Jenkins credential ID for dockerhub
    }
    stages {
        // Checkout Source Code
        stage('Git Checkout') { 
            steps {
                git branch: 'main', url: 'https://github.com/your-repo/shop-ease.git'
            }
        }

        // User Service Backend Build, SonarQube Analysis, and Quality Gate
        stage('Build "User Service" and SonarQube Analysis') {
            steps {
                dir('user-service') {
                    sh 'mvn -B -DskipTests clean package'
                    withSonarQubeEnv("$SONARQUBE") {
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }

        stage('Quality Gate For User Service') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // Product Service Backend Build, SonarQube Analysis, and Quality Gate
        stage('Build "Product Service"') {
            steps {
                dir('product-service') {
                    sh 'mvn -B -DskipTests clean package'
                    withSonarQubeEnv("$SONARQUBE") {
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }

        stage('Quality Gate For Product Service') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // Frontend Service Build
        stage('Build "Frontend Service"') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        // Docker Image Build
        stage ('Build Docker Image') {
            steps {
                sh "docker build -t ${USER_SERVICE_IMAGE} ./user-service"
                sh "docker build -t ${PRODUCT_SERVICE_IMAGE} ./product-service"
                sh "docker build -t ${FRONTEND_SERVICE_IMAGE} ./frontend"
                
            }
        }

        stage('Push Docker Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: '${DOCKER_CREDENTIALS}', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh "echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin"
                    sh "docker push ${USER_SERVICE_IMAGE}"
                    sh "docker push ${PRODUCT_SERVICE_IMAGE}"
                    sh "docker push ${FRONTEND_SERVICE_IMAGE}"
                }
            }
        }

        // Deploy to Kubernetes Using Helm
        stage('Deploy to Kubernetes via Helm') {
            steps {
                sh """
                helm upgrade --install shopease ${HELM_CHART_DIR} \
                --set userService.image.repository=${USER_SERVICE_IMAGE} \
                --set productService.image.repository=${PRODUCT_SERVICE_IMAGE} \
                --set frontend.image.repository=${FRONTEND_SERVICE_IMAGE}
                """
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