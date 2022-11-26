pipeline {
    agent any
    environment {
        MVN_SETTINGS = credentials('jenkins_maven_settings')
        OPS_HOME = "/home/bitnami/infrastructure-resources-private"
        TECH1_DOCKERHUB_REPOSITORY = "tech1-framework-b2b-mongodb-server"
        PATH = "${OPS_HOME}/bin:${env.PATH}"
    }
    options {
        buildDiscarder(logRotator(numToKeepStr:'5'))
    }
    triggers {
        pollSCM('H */4 * * *')
    }
    tools {
        jdk 'Java 11'
        maven 'Maven 3.6.3'
    }
    stages {
        stage('deploy') {
            steps {
                sh 'mvn -s $MVN_SETTINGS clean install deploy -Prelease'
            }
        }
        stage('sonar') {
            when {
                branch 'dev'
            }
            steps {
                script {
                    def scannerHome = tool 'Tech1 SonarQube Scanner';
                    withSonarQubeEnv('Tech1 SonarQube') {
                        sh "${scannerHome}/bin/sonar-scanner";
                    }
                }
            }
        }
        stage('docker :dev') {
            when {
                branch 'dev'
            }
            steps {
                dir('tech1-framework-b2b-mongodb-server') {
                    sh 'cp ${OPS_HOME}/docker/java11-v3/Dockerfile .'
                    sh 'docker-push-image-v2.sh $TECH1_DOCKERHUB_USERNAME $TECH1_DOCKERHUB_ACCESS_TOKEN ${TECH1_DOCKERHUB_REPOSITORY} dev'
                }
            }
        }
        stage('docker :prod') {
            when {
                branch 'master'
            }
            steps {
                dir('tech1-framework-b2b-mongodb-server') {
                    sh 'cp ${OPS_HOME}/docker/java11-v3/Dockerfile .'
                    sh 'docker-push-image-v2.sh $TECH1_DOCKERHUB_USERNAME $TECH1_DOCKERHUB_ACCESS_TOKEN ${TECH1_DOCKERHUB_REPOSITORY} prod'
                }
            }
        }
    }
    post {
        always {
            script {
                // https://issues.jenkins-ci.org/browse/JENKINS-46325, https://jenkins.io/doc/book/pipeline/jenkinsfile
                if (currentBuild.result == null) {
                    currentBuild.result = 'SUCCESS'
                }
            }
            alwaysNotifications()
        }
    }
}

def alwaysNotifications() {
    emailext (
        to: "$TECH1_CEO, $TECH1_CTO",
        subject: '${DEFAULT_SUBJECT}',
        body: '''${SCRIPT, template="pipeline-changes.template"}'''
    )
}
