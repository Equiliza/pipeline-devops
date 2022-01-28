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
			string(name: 'stage', defaultValue: '')
    		}
	
    		stages {
        		stage('PipeLine') {
            			steps {
                			script {
						if (params.stage.length() == 0) { 
							if (params.buildTool == "gradle") {
								gradle()
							} else {
								maven()
							}
						} else {
							def stages = params.stage.split(";")
							for (i=0; i < stages.size(); i++) { 
	    							env.PSTAGE = stages[i]
								if (params.buildTool == "gradle") {
									gradle()
								} else {
									maven()
								}
							}
						}
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

return this;
