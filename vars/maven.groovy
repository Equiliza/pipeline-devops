/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(String PipeLineType){

	figlet PipeLineType

	if (PipeLineType == 'CI') {
        	stage('Build') {
		    	env.STAGE=env.STAGE_NAME
			figlet "Stage: ${env.STAGE}"
                    	bat "./mvnw.cmd clean compile -e"                       
        	}
        	stage('Test') {
		    	env.STAGE=env.STAGE_NAME
			figlet "Stage: ${env.STAGE}"
                    	bat "./mvnw.cmd clean test -e"                       
        	}
        	stage('Jar') {
		    	env.STAGE=env.STAGE_NAME
			figlet "Stage: ${env.STAGE}"
                    	bat "./mvnw.cmd clean package -e"                       
        	}
        	stage('Sonar') {
		    	env.STAGE=env.STAGE_NAME
			figlet "Stage: ${env.STAGE}"
                    	def scannerHome = tool 'sonar-scanner';
                    	withSonarQubeEnv('sonar-server') {
                    	bat "C:/Users/Patric~1/.jenkins/tools/hudson.plugins.sonar.SonarRunnerInstallation/sonar-scanner/bin/sonar-scanner.bat -Dsonar.projectKey=ejemplo-maven-new2 -Dsonar.sources=src -Dsonar.java.binaries=build"
                    	}
        	}
        	stage('Run') {
		    	env.STAGE=env.STAGE_NAME
			figlet "Stage: ${env.STAGE}"
                    	bat "start /min mvnw spring-boot:run &"
                    	sleep 20
        	}
        	stage('TestApp') {
		    	env.STAGE=env.STAGE_NAME
			figlet "Stage: ${env.STAGE}"
                    	bat "start chrome http://localhost:8082/rest/mscovid/test?msg=testing"
        	}
		stage('NexusUpload') {
		    	env.STAGE=env.STAGE_NAME
			figlet "Stage: ${env.STAGE}"
		    	nexusPublisher nexusInstanceId: 'test-nexus', nexusRepositoryId: 'test.nexus',
		    	packages: [[$class: 'MavenPackage',
				mavenAssetList: [[classifier: '',
				extension: '',
				filePath: 'C:/Users/Patric~1/Desktop/Ejercicio/ejemplo-maven/build/DevOpsUsach2020-0.0.1.jar']],
				mavenCoordinate: [artifactId: 'DevOpsUsach2020',
				groupId: 'com.devopsusach2020',
				packaging: 'jar',
				version: '0.0.1']
				]]
		}
	} else {
        	stage('NexusDownload') {
		    	env.STAGE=env.STAGE_NAME
			figlet "Stage: ${env.STAGE}"
                    	bat "curl -X GET -u admin:Pelusa50# http://localhost:8081/repository/test.nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O"  
                    	bat "dir" 
        	}
        	stage('Run') {
		    	env.STAGE=env.STAGE_NAME
			figlet "Stage: ${env.STAGE}"
                    	bat "start /min mvnw spring-boot:run &"
                    	sleep 20
        	}
        	stage('TestApp') {
		    	env.STAGE=env.STAGE_NAME
			figlet "Stage: ${env.STAGE}"
                    	bat "start chrome http://localhost:8082/rest/mscovid/test?msg=testing"
        	}
		stage('NexusUpload') {
		    	env.STAGE=env.STAGE_NAME
			figlet "Stage: ${env.STAGE}"
		    	nexusPublisher nexusInstanceId: 'test-nexus', nexusRepositoryId: 'test.nexus',
		    	packages: [[$class: 'MavenPackage',
				mavenAssetList: [[classifier: '',
				extension: '',
				filePath: 'C:/Users/Patric~1/Desktop/Ejercicio/ejemplo-maven/build/DevOpsUsach2020-0.0.1.jar']],
				mavenCoordinate: [artifactId: 'DevOpsUsach2020',
				groupId: 'com.devopsusach2020',
				packaging: 'jar',
				version: '1.0.0']
				]]
		}
	}

}

return this;
