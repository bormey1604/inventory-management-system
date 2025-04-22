pipeline {
    agent any

    tools {
        maven 'Maven 3.9.9'  // Name of the Maven installation (set in Global Tool Configuration)
        jdk 'JDK 21'  // Name of the JDK installation (set in Global Tool Configuration)
    }

    environment {
        SPRING_PROFILES_ACTIVE = 'dev'  // Example: You can set the Spring profile here
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/bormey1604/inventory-management-system.git'  // URL of your Git repo
            }
        }

        stage('Build') {
            steps {
                script {
                    // Run the Maven build command
                    sh 'mvn clean install -DskipTests=true'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Run unit tests
                    sh 'mvn test'
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    // Package the Spring Boot application into a jar/war file
                    sh 'mvn package -DskipTests=true'
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    // Build the Docker image for your application
                    sh 'docker build -t inventory-management-system:latest .'
                }
            }
        }

        stage('Docker Run') {
            steps {
                script {
                    // Stop and remove any existing container before running a new one
                    sh 'docker ps -q -f name=inventory-management-system | xargs -r docker stop | xargs -r docker rm'

                    // Run the Docker container from the built image
                    sh 'docker run -d -p 8080:8080 --name inventory-management-system inventory-management-system:latest'
                }
            }
        }
    }

    post {
        always {
            // Clean up or perform actions after the build (e.g., sending notifications)
            echo 'Cleaning up after the build'
//             sh 'docker system prune -af'  // Remove unused Docker resources
        }

        success {
            // Actions for a successful pipeline run (e.g., notify team)
            echo 'Build and tests passed successfully!'
        }

        failure {
            // Actions for a failed pipeline run (e.g., notify team)
            echo 'Build failed. Investigate the issues.'
        }
    }
}
