/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
  
	pipeline {
    	agent any
	
    		parameters {
  			choice choices: ['gradle', 'maven'], description: 'Indicar Herramienta de Construcción', name: 'buildTool'
	
    		stages {
        		stage('PipeLine') {
            			steps {
                			script {
						if (params.buildTool == "gradle") { gradle(verifyBranchName()) } else { maven(verifyBranchName()) }
                			}
            			}
        		}
    		}
	
		post {
			success {
				slackSend color: 'good', message: "[${env.BUILD_USER}][${env.USUARIO}][${env.JOB_NAME}][${params.buildTool}] Ejecución Exitosa!"
				
			}
	
			failure {
				slackSend color: 'danger', message: "[${env.BUILD_USER}][${env.USUARIO}][${env.JOB_NAME}][${params.buildTool}] Ejecución fallida en stage ${env.STAGE}"
				error "Ejecución fallida en stage ${env.STAGE}"
			}
		}
	}

}

def verifyBranchName() {

	//def is_ci_or_cd = ( env.GIT_BRANCH.contains('feature-')) ? 'CI' : 'CD'

	if (env.GIT_BRANCH.contains('feature-') || env.GIT_BRANCH.contains('develop')) {
		return 'CI'
	} else {
		return 'CD'
	}
}

return this;
