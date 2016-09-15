package com.example.controller;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.DeltaUsersParserService;

/**
 * Webhooks are a way for web apps to get real-time notifications when users' files change in Dropbox. Once you register a URI to receive webhooks, Dropbox will
 * send an HTTP request to that URI every time there's a change for any of your app's registered users.
 */
@RestController
@RequestMapping("webhook")
public class DropboxWebhookController {

    private static final Logger LOG = getLogger(DropboxWebhookController.class);

    @Autowired
    private DeltaUsersParserService deltaUsersParserService;

    /**
     * Once you enter your webhook URI, an initial "verification request" will be made to that URI. This verification is an HTTP GET request with a query
     * parameter called challenge.
     */
    @RequestMapping(method = GET)
    public String getWebhookVerification(@RequestParam("challenge") final String challenge) {
        LOG.info("Respond to the webhook verification (GET request) by echoing back the challenge parameter.");
        return challenge;
    }

    /**
     * Once your webhook URI is added, your app will start receiving "notification requests" every time a user's files change. A notification request is an HTTP
     * POST request with a JSON body.
     */
    @RequestMapping(method = POST)
    public void getFileData(@RequestBody final String notificationBody) throws Exception {
        LOG.info("Receive a list of changed user IDs from Dropbox and process each: '{}'", notificationBody);
        final List<String> userIds = deltaUsersParserService.getUsers(notificationBody);
        for (final String userId : userIds) {
            LOG.info("changed user id: '{}'", userId);
        }
    }
}
