pipeline {
    agent any

    tools {
        dockerTool 'Docker'
        maven 'Maven 3.9.9'
        jdk 'JDK 21'
    }

    environment {
        SPRING_PROFILES_ACTIVE = 'dev'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/bormey1604/inventory-management-system.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install -DskipTests=true'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests=true'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    // Use Jenkins build number as image tag
                    env.IMAGE_TAG = "${env.BUILD_NUMBER}"
                    sh "docker build -t inventory-management-system:${IMAGE_TAG} ."
                }
            }
        }

        stage('Trigger Deploy to Kubernetes') {
            steps {
                script {
                    build job: 'deploy-to-k8s', wait: true, parameters: [
                        string(name: 'SPRING_PROFILES_ACTIVE', value: "${env.SPRING_PROFILES_ACTIVE}"),
                        string(name: 'MONGO_URI', value: "${env.MONGO_URI}"),
                        string(name: 'IMAGE_TAG', value: "${env.IMAGE_TAG}")
                    ]
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up after the build'
        }

        success {
            echo "Build and tests passed. Deployed with image tag: ${env.IMAGE_TAG}"
        }

        failure {
            echo 'Build or deployment failed. Investigate the issues.'
        }
    }
}
