timeout(30) {
    node('maven') {
        timestamps {
            stage('Checkout') {
                checkout scm
            }
            stage('Run tests') {
                tests_exit_code = sh(
                        script: """"
                mvn test -Dbrowser=$BROWSER -Dbrowserversion=$BROWSER_VERSION -Dwebdriver.base.url=$BASE_URL -Dwebdriver.remote.url=$SELENOID_URL
                """,
                        returnStatus: true
                )
                if (tests_exit_code != 0) {
                    currentBuild.result = 'UNSTABLE'
                }
            }
            stage('Publish artifacts') {
                sh "echo BASE_URL=$BASE_URL > target/allure-results/environment.properties"
                sh "echo BROWSER=$BROWSER >> target/allure-results/environment.properties"
                sh "echo BROWSER_VERSION=$BROWSER_VERSION >> target/allure-results/environment.properties"
                sh "echo SELENOID_URL=$SELENOID_URL >> target/allure-results/environment.properties"
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