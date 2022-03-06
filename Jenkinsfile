def PROJECT_NAME = "simple-api"
def gitUrl = "https://github.com/skaqud/${PROJECT_NAME}.git"
def imgRegistry = "https://registry.hub.docker.com"
def gitOpsUrl = "github.com/skaqud/simple-gitops.git"
def opsBranch = "main"
/////////////////////////////
pipeline {
     environment {
         PATH = "$PATH:/usr/local/bin/"  //maven, skaffold, argocd,jq path
       }
    agent any
    stages {
        stage('Build') {
            steps {
                checkout scm: [
                        $class: "GitSCM",
                        userRemoteConfigs: [[url: "${gitUrl}",
                        credentialsId: "git-credential" ]],     //credential 이름이 jenkins에 등록된 이름과 동일해야 함
                        branches: [[name: "refs/tags/${TAG}"]]],
                    poll: false
                script{
                    docker.withRegistry("${imgRegistry}","imageRegistry-credential"){   //credential 이름이 jenkins에 등록된 이름과 동일해야 함, jenkins에 docker deploy 권한 필요
                        sh "skaffold build -p dev -t ${TAG}"
                    }
                    // mac local 일때만 사용 linux 환경에서는 docker.withRegistry 사용
                    sh "skaffold build -p dev -t ${TAG}"
                }
            }
        }
        stage('workspace clear'){
            steps {
                cleanWs()
            }
        }
        stage('GitOps update') {
            steps{
                print "======kustomization.yaml tag update====="
                git url: "https://${gitOpsUrl}", branch: "main" , credentialsId: "git-credential"
                script{
                    sh """
                        cd ./simple-api/blue-green
                        kustomize edit set image skaqud/simple-api:${TAG}
                        # 로컬외에는 주석 제거한다
                        git config --global user.email "admin@demo.com"
                        git config --global user.name "admin"
                        git add .
                        git commit -am 'update image tag ${TAG}'
                        git remote set-url --push origin https://${gitOpsUrl}
                        git push origin ${opsBranch}
                    """
                 }
                print "git push finished !!!"
            }
        }
    }
}