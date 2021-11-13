def buildAppDEV() {
    echo "-------------------------------------------------------------------"
    sh "${comandossh} ' docker build --build-arg JAR_FILE=products-service-example.jar -t 127.0.0.1:5000/products-service:${branchName} ./ac1cicd-${branchName} '"
}

def buildAppTEST() {
    echo "-------------------------------------------------------------------"
    sh "${comandossh} ' docker build --build-arg JAR_FILE=products-service-example.jar -t 127.0.0.1:5000/products-service:${branchName} ./ac1cicd-${branchName} '"
} 

def buildAppPROD() {
    echo "-------------------------------------------------------------------"
    sh "${comandossh} ' docker build --build-arg JAR_FILE=products-service-example.jar -t 127.0.0.1:5000/products-service:${branchName} ./ac1cicd-${branchName} '"
} 

def testApp() {
    echo 'testing the application...'
} 

def deployAppPROD() {
    sh "${comandossh} 'docker run -d --name products-service-${branchName} -p 81:8080  127.0.0.1:5000/products-service:${branchName}'"
} 

def deployAppTEST() {
    sh "${comandossh} 'docker run -d --name products-service-${branchName} -p 81:8080  127.0.0.1:5000/products-service:${branchName}'"
}

def deployAppDEV() {
    sh "${comandossh} 'docker run -d --name products-service-${branchName} -p 81:8080  127.0.0.1:5000/products-service:${branchName}'"
}


return this
