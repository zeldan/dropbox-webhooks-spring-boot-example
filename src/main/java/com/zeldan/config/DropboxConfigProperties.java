package com.zeldan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("dropbox.app")
public class DropboxConfigProperties {

    private final SessionStore sessionStore = new SessionStore();

    private String key;

    private String secret;

    private String redirectUri;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(final String secret) {
        this.secret = secret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(final String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public SessionStore getSessionStore() {
        return sessionStore;
    }

    public static class SessionStore {

        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(final String key) {
            this.key = key;
        }

    }

}
