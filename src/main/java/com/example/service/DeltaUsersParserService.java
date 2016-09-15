package com.example.service;

import static java.util.Collections.emptyList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * Parses users' ids information from the Dropbox Webhook notification message.
 * It only contains users, who make changes.
 */
@Service
public class DeltaUsersParserService {

    private static final Logger LOG = getLogger(DeltaUsersParserService.class);
    private static final Gson GSON = new Gson();

    public List<String> getUsers(final String notification) {
        List<String> users = null;
        try {
            final JsonArray jsonUsers = getJsonUsersFromNotificationBody(notification);
            users = GSON.fromJson(jsonUsers, new TypeToken<List<String>>() {
            }.getType());
            LOG.debug("Parse users successfully from notification: '{}'", notification);
        } catch (final Exception e) {
            LOG.error("An error occured while try to parse users: ", e);
            users = emptyList();
        }
        return users;
    }

    private JsonArray getJsonUsersFromNotificationBody(final String notification) {
        final JsonObject rootObject = new JsonParser().parse(notification).getAsJsonObject();
        final JsonArray jsonUsers = rootObject.getAsJsonObject("delta").getAsJsonArray("users");
        return jsonUsers;
    }
}
