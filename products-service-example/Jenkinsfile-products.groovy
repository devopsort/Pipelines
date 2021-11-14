pipeline {
    agent { label 'master' }
    triggers {
       // sondea el repositorio cada 2 minutos para ver los cambios
       pollSCM('*/2 * * * *')
    }
    parameters {
        //choice(name: "Enviromnment", choices: ["Developer", "Testing", "main"], description: "Environment deploy (Release=main)") 
        string(name: "AppVersion", defaultValue: "1.0.0", description: "App Version Code (X.Y.Z)")
    }
    stages {
        stage("Init") {
            steps {
                script {
                    comandossh = "ssh -i /var/jenkins_home/keyssh-devops-curso.pem ec2-user@JenkinsDockerTF sudo "
                    //RepoURL = "https://github.com/devopsort/products-service-example/archive/refs/heads/"    //${RepoURL}
                    ENV_NAME = "${env.BRANCH_NAME}"
                    }
                }
        }                
        stage('CloneRepo') {
            steps {
                echo 'Pulling...' + ENV_NAME
                checkout scm
            
            }
           /* steps {
                echo "-------------------------------------------------------------------"
                sh "${comandossh} ' rm -rf *.zip'"
                sh "${comandossh} ' wget ${RepoURL}${env.BRANCH_NAME}.zip'"
                sh "${comandossh} ' unzip -o ${env.BRANCH_NAME}.zip'"
            }*/
        }
        stage('DockerBuild') {
            steps {
                echo "-------------------------------------------------------------------"
                sh "${comandossh} ' docker build --build-arg JAR_FILE=products-service-example.jar -t 127.0.0.1:5000/products-service:${ENV_NAME} ./ac1cicd-${ENV_NAME} '"
            }
        }   
        stage('DockerRegistry') {
            steps {
                echo "-------------------------------------------------------------------"
                sh "${comandossh} '   docker push 127.0.0.1:5000/products-service:${ENV_NAME}'"
            }
        }  

        stage('StopDocker') {
            steps {
                echo "-------------------------------------------------------------------"
                sh "${comandossh} 'docker stop products-service-${ENV_NAME} || true && sudo docker rm products-service-${ENV_NAME} || true'"
            }
        }
        stage('Deploy') {
            steps {
                script {
                    if (ENV_NAME == 'Dev') {
                    
                    sh "${comandossh} 'docker run -d --name products-service-${ENV_NAME} -p 80:8080  127.0.0.1:5000/products-service:${ENV_NAME}'"
                
                    } else if (ENV_NAME == 'Test') {
                            
                        sh "${comandossh} 'docker run -d --name products-service-${ENV_NAME} -p 81:8080  127.0.0.1:5000/products-service:${ENV_NAME}'"
                    
                    } else {

                        sh "${comandossh} 'docker run -d --name products-service-${ENV_NAME} -p 82:8080  127.0.0.1:5000/products-service:${ENV_NAME}'"

                        }
                    }     
                }             
            }
        }
    }