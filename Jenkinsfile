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
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        /* ✅ ONE Maven command:
         * - compiles
         * - runs tests
         * - collects JaCoCo execution data
         * - generates jacoco.xml + HTML
         */
        stage('Build, Test & Coverage') {
            steps {
                bat 'mvn clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    bat """
                    sonar-scanner ^
                    -Dsonar.projectKey=localization-app ^
                    -Dsonar.projectName=Localization-App ^
                    -Dsonar.sources=src/main/java ^
                    -Dsonar.tests=src/test/java ^
                    -Dsonar.java.binaries=target/classes ^
                    -Dsonar.junit.reportPaths=target/surefire-reports ^
                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    """
                }
            }
        }

        stage('Publish Coverage Report') {
            when {
                expression { fileExists('target/site/jacoco/index.html') }
            }
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
                    def dockerImage = docker.build("${IMAGE}", ".")
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                bat 'docker context use default'
                script {
                    docker.withRegistry(
                            'https://registry.hub.docker.com',
                            'dockerhub-login'
                    ) {
                        docker.build("${IMAGE}", ".").push()
                    }
                }
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/**/*.xml'
        }
        success {
            echo 'Build succeeded ✅'
        }
        failure {
            echo 'Build failed ❌'
        }
    }
}