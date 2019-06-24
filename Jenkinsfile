pipeline {
    agent any
    tools {
        maven 'maven-3.5.4'
        jdk 'graalvm'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    which java
                    which mvn
                ''' 
            }
        }
        stage('Build') {
            steps {
                sh './mvnw clean install -DskipTests -DskipITs'
            }
        }
        stage('Test JVM') {
            steps {
                sh './mvnw verify'
            }
        }
        stage('Test Native') {
            steps {
                sh './mvnw verify -Dnative'
            }
        }
        stage('Results') {
            steps {
                sh 'du -cskh */target/* | grep -E "target/scenario|target/lib"'
            }
            post {
                always {
                    junit '**/target/surefire-reports/**/*.xml' 
                }
            }
        }
    }
}