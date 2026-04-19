pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
        jdk 'JAVA_21'
    }

    environment {
        DOCKERHUB_REPO = 'mikukoskela/localization-app'
        DOCKER_IMAGE_TAG = 'latest'
        IMAGE = "${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}"

         // SONAR_TOKEN = credentials('SONAR_TOKEN')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Clean & Compile') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Maven Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Maven Package') {
            steps {
                bat 'mvn package'
            }
        }


        stage('SonarQube Analysis') {
            when {
                expression { fileExists('target/classes') }
            }
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    bat """
            ${tool 'SonarScanner'}\\bin\\sonar-scanner ^
            -Dsonar.projectKey=localization-app ^
            -Dsonar.projectName=Localization-App ^
            -Dsonar.sources=src ^
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

        stage('Publish Coverage Report') {
            steps {
                publishHTML(target: [
                        reportDir: 'target/site/jacoco',
                        reportFiles: 'index.html',
                        reportName: 'JaCoCo Coverage',
                        keepAll: true,
                        alwaysLinkToLastBuild: true
                ])
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${IMAGE}", ".")
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry(
                            'https://registry.hub.docker.com',
                            'dockerhub-login'
                    ) {
                        dockerImage.push()
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                junit 'target/surefire-reports/**/*.xml'
            }
        }
        success {
            echo 'Build succeeded ✅'
        }
        failure {
            echo 'Build failed ❌'
        }
    }
}