pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
        jdk 'JAVA_21'
    }
    environment {
        DOCKERHUB_REPO = 'mikukoskela/localization-app'
        DOCKER_IMAGE_TAG = "latest"
        IMAGE = "${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}"
    }


    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('mvn clean and compile') {
            steps {
                bat 'mvn clean compile'            }
        }

        stage('mvn test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('mvn deploy') {
            steps {
                bat 'mvn package'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    bat """
        ${tool 'SonarScanner'}\\bin\\sonar-scanner ^
        -Dsonar.projectKey=devops-demo ^
        -Dsonar.sources=src ^
        -Dsonar.projectName=DevOps-Demo ^
        -Dsonar.host.url=http://localhost:9000 ^
        -Dsonar.login=${env.SONAR_TOKEN} ^
        -Dsonar.java.binaries=target/classes
        """
                }
            }
        }

        stage('Generate Coverage Report') {
            steps {
                bat 'mvn jacoco:report'
            }
        }

        stage("Publish Coverage report") {
            steps {
                publishHTML(target: [
                        reportDir: 'target/site/jacoco',
                        reportFiles: 'index.html',
                        reportName: 'JaCoCo Coverage'
                ])
            }
        }

        stage("Build Docker Image") {
            steps {
                script {
                    dockerImage = docker.build("${IMAGE}", ".")
                }
            }
        }

        stage("Push Docker Image to Docker Hub") {
            steps {
                bat "docker context use default"
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub-login') {
                        dockerImage.push()
                    }
                }
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
        success {
            echo 'Build succeeded'
        }
        failure {
            echo 'Build failed'
        }
    }
}