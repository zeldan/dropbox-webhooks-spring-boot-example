package com.zeldan.service;

import static java.util.Collections.emptyList;
import static org.slf4j.LoggerFactory.getLogger;

import java.lang.reflect.Type;
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

    public List<String> getUsers(String notification) {
        try {
            JsonArray jsonUsers = getJsonUsersFromNotificationBody(notification);
            List<String> users = GSON.fromJson(jsonUsers, createStringListTypeToken());
            LOG.debug("Parse users successfully from notification: '{}'", notification);
            return users;
        } catch (Exception e) {
            LOG.error("An error occurred while trying to parse users: ", e);
            return emptyList();
        }
    }

    private Type createStringListTypeToken() {
        return new TypeToken<List<String>>() {}.getType();
    }

    private JsonArray getJsonUsersFromNotificationBody(String notification) {
        JsonObject rootObject = new JsonParser().parse(notification).getAsJsonObject();
        return rootObject.getAsJsonObject("delta").getAsJsonArray("users");
    }
}
