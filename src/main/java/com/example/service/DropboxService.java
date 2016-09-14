package com.example.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;

@Service
public class DropboxService {

    private static final String REDIRECT_URI = "http://localhost:8080/dropbox/finish-auth";

    @Value("${dropbox.app.sessionstore.key}")
    private String sessionStoreKey;

    @Autowired
    private DbxWebAuth auth;

    public String dropboxStartAuth(final HttpSession session) {
        final DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, sessionStoreKey);
        final DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
                .withRedirectUri(REDIRECT_URI, csrfTokenStore)
                .build();
        return auth.authorize(authRequest);
    }

}
