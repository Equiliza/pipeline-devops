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
	    		PSTAGE = ''
		}
	
    		parameters {
  			choice choices: ['gradle', 'maven'], description: 'Indicar Herramienta de Construcción', name: 'buildTool'
			string(name: 'stage', defaultValue: '')
    		}
	
    		stages {
        		stage('PipeLine') {
            			steps {
                			script {
						def stages = params.stage.split(";")
						println stages.size() 
						if (params.stage != '') { 
							for (i=0; i < stages.size(); i++) { 
	    							PSTAGE = stages[i]
								println stages[i]
								println "${PSTAGE}"
								if (params.buildTool == "gradle") {
									gradle()
								} else {
									maven()
								}
							}
						} else {
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
	
		post {
			success {
                		script {
					if (env.STAGE != null) {
						slackSend color: 'good', message: "[${env.BUILD_USER}][${env.USUARIO}][${env.JOB_NAME}][${params.buildTool}] Ejecución Exitosa!"
					} else {
						slackSend color: 'danger', message: "[${env.BUILD_USER}][${env.USUARIO}][${env.JOB_NAME}][${params.buildTool}] Ejecución fallida en stage ${params.stage}"
						error "Ejecución fallida en stage ${params.stage}"
					}
				}
			}
	
			failure {
				slackSend color: 'danger', message: "[${env.BUILD_USER}][${env.USUARIO}][${env.JOB_NAME}][${params.buildTool}] Ejecución fallida en stage ${STAGE}"
				error "Ejecución fallida en stage ${STAGE}"
			}
		}
	}

}

return this;
