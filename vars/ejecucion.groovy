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
						if (params.stage.length() == 0) { 
							println "cero"
							if (params.buildTool == "gradle") {
								gradle()
							} else {
								maven()
							}
						} else {
							println "no cero"
							def stages = params.stage.split(";")
							for (i=0; i < stages.size(); i++) { 
	    							PSTAGE = stages[i]
								if (params.buildTool == "gradle") {
									gradle()
								} else {
									maven()
								}
								if (env.STAGE == null) {
									slackSend color: 'danger', message: "[${env.BUILD_USER}][${env.USUARIO}][${env.JOB_NAME}][${params.buildTool}] Ejecución fallida en stage ${env.PSTAGE}"
									error "Ejecución fallida en stage ${env.PSTAGE}"
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
				slackSend color: 'danger', message: "[${env.BUILD_USER}][${env.USUARIO}][${env.JOB_NAME}][${params.buildTool}] Ejecución fallida en stage ${STAGE}"
				error "Ejecución fallida en stage ${STAGE}"
			}
		}
	}

}

return this;
