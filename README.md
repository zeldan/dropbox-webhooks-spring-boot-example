# dropbox-webhooks-spring-boot-example
Dropbox webhooks example application in Spring Boot 2

## About the project
Webhooks are a way for web apps to get real-time notifications when users' files change in Dropbox.
Once you register a URI to receive webhooks, Dropbox will send an HTTP request to that URI every time there's a change for any of your app's registered users

The server is keeping track of each user's OAuth access token and their latest cursor in Redis (a key-value store). 

## Technology Stack
- Java 8
- Spring boot 2.1.3
- Dropbox Core SDK 3.0.11
- Redis

## Usage
### Environment settings
1. creating a new app in DropBox / My apps [here](https://www.dropbox.com/developers/apps)
2. add  `http://localhost:8080/dropbox/finish-auth` redirect uri to OAuth 2 / Redirect URIs
3. add webhook uri `http://<your ip address>:8080/webhook` to Webhooks
4. change `dropbox.app.key` and `dropbox.app.secret` values in `application.yml`
5. start redis locally

### Run the project
1. run "gradlew bootRun"
2. GET http://localhost:8080/dropbox/start-auth
3. allow it

After the above settings, you will see entries about the changes in the console log, if you modify files / directories in you dropbox directory. An example about adding and deleting a file:

```
c.z.controller.DropboxWebhookController  : changed user id: '43879136'
com.zeldan.service.DropboxService        : Changed metadata: '{".tag":"file","name":"mysql-init.txt","id":"id:labNcjCFUWMAAAAAAAAFSw","client_modified":"2018-09-14T19:27:55Z","server_modified":"2018-09-14T21:02:54Z","rev":"6ae043217ec","size":50,"path_lower":"/mysql-init.txt","path_display":"/mysql-init.txt","content_hash":"9930a1c4bfd5ea3c1f2e170857587a015dcf42f8d070be19236cea174e03f474"}'
c.z.controller.DropboxWebhookController  : Receive a list of changed user IDs from Dropbox and process each: '{"list_folder": {"accounts": ["dbid:AAB05qlghuWE14Vp4ZPFnus77LtWeMnAAz4"]}, "delta": {"users": [43879136]}}'
c.z.controller.DropboxWebhookController  : changed user id: '43879136'
com.zeldan.service.DropboxService        : Changed metadata: '{".tag":"deleted","name":"mysql-init.txt","path_lower":"/mysql-init.txt","path_display":"/mysql-init.txt"}'
c.z.controller.DropboxWebhookController  : Receive a list of changed user IDs from Dropbox and process each: '{"list_folder": {"accounts": ["dbid:AAB05qlghuWE14Vp4ZPFnus77LtWeMnAAz4"]}, "delta": {"users": [43879136]}}'
```
