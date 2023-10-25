timeout(30) {
    node('maven') {
        timestamps {
            stage('Checkout') {
                checkout scm
            }
            stage('Run tests') {
                tests_exit_code = sh(
                        script: """
                mvn -Dbrowser=$BROWSER -Dbrowserversion=$BROWSER_VERSION -Dwebdriver.base.url=$BASE_URL -Dwebdriver.remote.url=$SELENOID_URL -Djunit.jupiter.execution.parallel.enabled=true -Djunit.jupiter.execution.parallel.mode.default=same_thread -Djunit.jupiter.execution.parallel.mode.classes.default=concurrent test
                """,
                        returnStatus: true
                )
                if (tests_exit_code != 0) {
                    currentBuild.result = 'UNSTABLE'
                }
            }
            stage('Publish artifacts') {
                sh "echo BASE_URL=$BASE_URL > ./allure-results/environment.properties"
                sh "echo BROWSER=$BROWSER >> ./allure-results/environment.properties"
                sh "echo BROWSER_VERSION=$BROWSER_VERSION >> ./allure-results/environment.properties"
                sh "echo SELENOID_URL=$SELENOID_URL >> ./allure-results/environment.properties"
                allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: './allure-results']]
                ])
            }
        }
    }
}