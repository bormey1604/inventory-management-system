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

        stage('Test Docker') {
            steps {
                sh 'docker --version'
                sh 'docker ps'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t inventory-management-system:latest .'
            }
        }

        stage('Docker Run') {
            steps {
                script {
                    // Stop and remove the old container if it exists
                    sh '''
                        docker ps -q -f name=inventory-management-system | grep -q . && docker stop inventory-management-system && docker rm inventory-management-system || true
                    '''

                    // Run the Docker container
                    sh 'docker run -d -p 8080:8080 --name inventory-management-system inventory-management-system:latest'
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
            echo 'Build and tests passed successfully!'
        }

        failure {
            echo 'Build failed. Investigate the issues.'
        }
    }
}
