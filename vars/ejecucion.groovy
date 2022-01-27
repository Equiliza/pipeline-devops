/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
  
	pipeline {
    	agent any
	
		environment {
	    	STAGE = ''
		}
	
    		parameters {
  			choice choices: ['gradle', 'maven'], description: 'Indicar Herramienta de Construcción', name: 'buildTool'
    		}
	
    		stages {
        		stage('PipeLine') {
            			steps {
                			script {
						if (params.buildTool == "gradle") {
							gradle()
						} else {
							maven()
						}
                			}
            			}
        		}
    		}
	
		post {
			success {
				slackSend color: 'good', message: 'Ejecución Exitosa!'
			}
	
			failure {
				slackSend color: 'danger', message: "[${env.BUILD_USER}][${env.USUARIO}][${env.JOB_NAME}][${params.buildTool}] Ejecución fallida en stage ${STAGE}"
				error "Ejecución fallida en stage ${STAGE}"
			}
		}
	}

}

return this;
