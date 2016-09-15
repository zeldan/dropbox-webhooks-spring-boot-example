# dropbox-webhooks-spring-boot-example
Dropbox webhooks example application in Spring Boot

## About the project
Webhooks are a way for web apps to get real-time notifications when users' files change in Dropbox.
Once you register a URI to receive webhooks, Dropbox will send an HTTP request to that URI every time there's a change for any of your app's registered users

## Technology Stack
- Java 8
- Spring boot 1.4
- Dropbox Core SDK 2.1.1
- Redis

## Usage
1. creating a new app in DropBox / My apps [here](https://www.dropbox.com/developers)
2. add  `http://localhost:8080/dropbox/finish-auth` redirect uri to OAuth 2 / Redirect URIs
3. add webhook uri `http://<your ip address>:<port>/webhook` to Webhooks
4. change `dropbox.app.key` and `dropbox.app.secret` values in `application.properties`
5. start redis
6. run "gradlew bootRun"
7. GET http://localhost:8080/dropbox/start-auth
