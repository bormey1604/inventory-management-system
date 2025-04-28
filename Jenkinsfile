pipeline {
    agent any

    tools {
        dockerTool 'Docker'                // Name must match Global Tool Configuration
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
                sh 'docker build -t inventory-management-system:latest .'
            }
        }

        stage('Trigger Deploy to Kubernetes') {
            steps {
                script {
                    // Trigger the "deploy-to-k8s" job
                    build job: 'deploy-to-k8s', wait: true, parameters: [
                        string(name: 'SPRING_PROFILES_ACTIVE', value: "${env.SPRING_PROFILES_ACTIVE}"),
                        string(name: 'MONGO_URI', value: "${env.MONGO_URI}")
                    ]
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up after the build'
            // Optionally prune docker resources
            // sh 'docker system prune -af'
        }

        success {
            echo 'Build and tests passed successfully! Deployment triggered to Kubernetes.'
        }

        failure {
            echo 'Build or deployment failed. Investigate the issues.'
        }
    }
}
