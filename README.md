# dropbox-webhooks-spring-boot-example
Dropbox webhooks example application in Spring Boot

## About the project
Webhooks are a way for web apps to get real-time notifications when users' files change in Dropbox.
Once you register a URI to receive webhooks, Dropbox will send an HTTP request to that URI every time there's a change for any of your app's registered users

The server is keeping track of each user's OAuth access token and their latest cursor in Redis (a key-value store). 

## Technology Stack
- Java 8
- Spring boot 1.5.2
- Dropbox Core SDK 2.1.2
- Redis

## Usage
### Environment settings
1. creating a new app in DropBox / My apps [here](https://www.dropbox.com/developers)
2. add  `http://<your ip address>:<port>/dropbox/finish-auth` redirect uri to OAuth 2 / Redirect URIs
3. add webhook uri `http://<your ip address>:<port>/webhook` to Webhooks
4. change `dropbox.app.key` and `dropbox.app.secret` values in `application.properties`
5. start redis

### Run the project
1. run "gradlew bootRun"
2. GET http://localhost:8080/dropbox/start-auth
3. allow it

After the above settings, you will see entries about the changes in the console log, if you modify files / directories in you dropbox directory.
