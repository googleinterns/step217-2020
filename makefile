JFLAGS = -g
JC = javac
JVM= java 

make console:
	npm install
	# build react application
	cd src/main/webapp && npm install && npm run build
	# run java and node servers and react app concurrently
	npm run dev:console

make shell:
	npm install
	# build react application
	cd src/main/webapp && npm install && npm run build
	# run java and node servers and react app concurrently
	npm run dev:shell

make deploy: 
	gcloud init
	gcloud config set project $(projectID)
	npm install
	# Deploy Node JS and build frontend
	cd src/main/webapp && npm install && npm run build && gcloud app deploy
	# Deploy Java backend
	mvn package appengine:deploy -Dapp.deploy.projectId=$(projectID)
	# Configurate routing (send some requests to Node server and others to Java server)
	gcloud app deploy dispatch.yaml  
