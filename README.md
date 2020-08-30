# Alpollo

Capstone project for STEP 2020

## Add sensitive data locally

Add project ID to `src/main/webapp/WEB-INF/config.json`

Add API-KEY and other sensitive data you need for Node JS to `src/main/webapp/.env`

*** DO NOT PUSH THESE FILES TO GIT ***

In case you did this fatal mistake: change API-key/Client ID in GCP and inform others about it and don't forget to do deploy with new keys.

## Run locally

Use the next commands (start from the project folder) to run the application locally:

```
npm install
# build react application
cd src/main/webapp
npm install
npm run build
# go back to the project folder
cd ../../../
# run java and node servers and react app concurrently
npm run dev:console
```

For running Node and Java servers and React app at the same time we use `concurrently`, which will separate this three processes to three threads.

This message in terminal will show that Node JS server started to run successfully: `Node JS server listening on port 5000!`

It's possible that you can see this message: `Something is already running on port ${port number}.` It usually means that we can't run React App on this port because it's occupied by smth else. ***@jeinygroove*** changed the default (3000) port to 3006 and this error doesn't occurred anymore, but if you'll have this problem, try the next things: 

```
# Find
sudo lsof -i :<port number>
# Kill
kill -9 <PID>
```

## Run in Cloud Shell

Do the same command as above, but the last command will be `npm run dev:shell` instead of `npm run dev:console` (The only difference is that the first one runs tests and the second skips them)

## Deploy

Use the next commands (start from the project folder) to deploy the application:

```
gcloud init
gcloud config set project [Project_ID]
npm install
# Deploy Node JS and build frontend
cd src/main/webapp
npm install
npm run build
gcloud app deploy
# Deploy Java backend
cd ../../../
mvn package appengine:deploy -Dapp.deploy.projectId=[Project_ID]
# Configurate routing (send some requests to Node server and others to Java server)
gcloud app deploy dispatch.yaml
```

### Why so many servers?

Since we started to make requests to APIs from frontend, we need to hide API keys/Cliend IDs and so on. Why do we need Node JS server?

- We can't use Java backend and make requests to it, because everyone will be able to see response with API key.
- We also can't do these request from Java backend (OK, maybe it's possible, if you know how, please, tell us)
- We also can't manually add this data to `*.js` file before deploying, because they're also visible from the Inspector

That's why we used quite standart solution: add Node JS server, which will make these requests for us. And Node JS take this sensitive data from `.env` file, using this kind of command: `process.env.API_KEY`. 

### Clues for future development

- If you want to make a request from frontend, which ***DON'T*** use sensitive data, feel free to do it as before. In other cases add this request to `server.js` (**Important** this request should start with `/api`, we need this for proper routing)
 
