package com.example.controller;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.DeltaUsersParserService;
import com.example.service.DropboxService;

/**
 * Webhooks are a way for web apps to get real-time notifications when users' files change in Dropbox. Once you register a URI to receive webhooks, Dropbox will
 * send an HTTP request to that URI every time there's a change for any of your app's registered users.
 */
@RestController
@RequestMapping("webhook")
public class DropboxWebhookController {

    private static final Logger LOG = getLogger(DropboxWebhookController.class);

    private final DeltaUsersParserService deltaUsersParserService;

    private final DropboxService dropboxService;

    public DropboxWebhookController(final DeltaUsersParserService deltaUsersParserService, final DropboxService dropboxService) {
        this.deltaUsersParserService = deltaUsersParserService;
        this.dropboxService = dropboxService;
    }

    /**
     * Once you enter your webhook URI, an initial "verification request" will be made to that URI. This verification is an HTTP GET request with a query
     * parameter called challenge.
     */
    @GetMapping
    public String getWebhookVerification(@RequestParam("challenge") final String challenge) {
        LOG.info("Respond to the webhook verification (GET request) by echoing back the challenge parameter.");
        return challenge;
    }

    /**
     * Once your webhook URI is added, your app will start receiving "notification requests" every time a user's files change. A notification request is an HTTP
     * POST request with a JSON body.
     */
    @PostMapping
    public void getFileData(@RequestBody final String notificationBody) throws Exception {
        LOG.info("Receive a list of changed user IDs from Dropbox and process each: '{}'", notificationBody);
        final List<String> userIds = deltaUsersParserService.getUsers(notificationBody);
        for (final String userId : userIds) {
            LOG.info("changed user id: '{}'", userId);
            dropboxService.logChangedFiles(userId);
        }
    }
}
