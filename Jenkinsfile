pipeline {
    agent any
    environment {
        MVN_SETTINGS = credentials('jenkins_maven_settings')
        OPS_HOME = "/home/bitnami/infrastructure-resources-private"
        TECH1_MONGODB_DOCKERHUB_REPOSITORY = "tech1-framework-b2b-mongodb-server"
        TECH1_POSTGRES_DOCKERHUB_REPOSITORY = "tech1-framework-b2b-postgres-server"
        PATH = "${OPS_HOME}/bin:${env.PATH}"
    }
    options {
        buildDiscarder(logRotator(numToKeepStr:'5'))
    }
    triggers {
        pollSCM('H */4 * * *')
    }
    tools {
        jdk 'Java 17'
        maven 'Maven 3.6.3'
    }
    stages {
        stage('deploy') {
            steps {
                sh 'mvn -s $MVN_SETTINGS clean install deploy -Prelease'
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
