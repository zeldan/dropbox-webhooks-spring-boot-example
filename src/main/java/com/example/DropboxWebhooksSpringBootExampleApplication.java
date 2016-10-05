package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * Webhooks are a way for web apps to get real-time notifications when users' files change in Dropbox. Once you register a URI to receive webhooks, Dropbox will
 * send an HTTP request to that URI every time there's a change for any of your app's registered users
 *
 * The server is keeping track of each user's OAuth access token and their latest cursor in Redis (a key-value store).
 *
 * @author zeldan
 *
 */
@SpringBootApplication
public class DropboxWebhooksSpringBootExampleApplication {

    public static void main(final String[] args) {
        SpringApplication.run(DropboxWebhooksSpringBootExampleApplication.class, args);
    }
}
