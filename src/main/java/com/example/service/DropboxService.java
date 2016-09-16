package com.example.service;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.example.repository.UserTokenRepository;

@Service
public class DropboxService {

    private static final Logger LOG = getLogger(DropboxService.class);

    private static final String REDIRECT_URI = "http://localhost:8080/dropbox/finish-auth";
    private static final String CURSORS_HASH_KEY = "cursors";
    private static final String TOKENS_HASH_KEY = "tokens";

    @Value("${dropbox.app.sessionstore.key}")
    private String sessionStoreKey;

    @Autowired
    private DbxWebAuth auth;

    @Autowired
    private DbxRequestConfig requestConfig;

    @Autowired
    private UserTokenRepository userTokenRepository;

    public void logChangedFiles(final String userId) throws Exception {
        final String accessToken = getTokenAndCheckIsTokenExists(userId);
        final DbxClientV2 client = new DbxClientV2(requestConfig, accessToken);
        final String cursor = userTokenRepository.getValue(CURSORS_HASH_KEY, userId);
        final ListFolderResult listFolderContinue = client.files().listFolderContinue(cursor);
        logChangedFilesOfUser(userId, listFolderContinue);
    }

    public String startAuth(final HttpSession session) {
        final DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, sessionStoreKey);
        final DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
                .withRedirectUri(REDIRECT_URI, csrfTokenStore)
                .build();
        return auth.authorize(authRequest);
    }

    public void finishAuthAndSaveUserDetails(final HttpSession session, final Map<String, String[]> parameterMap) throws Exception {
        final DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, sessionStoreKey);
        final DbxAuthFinish authFinish = auth.finishFromRedirect(REDIRECT_URI, csrfTokenStore, parameterMap);
        final String accessToken = authFinish.getAccessToken();
        final DbxClientV2 client = new DbxClientV2(requestConfig, accessToken);
        final String userId = authFinish.getUserId();
        final ListFolderResult listFolderResult = client.files().listFolderBuilder("").withRecursive(true).start();
        saveAccessTokenAndActualCursor(userId, accessToken, listFolderResult);
    }

    private void saveAccessTokenAndActualCursor(final String userId, final String accessToken, final ListFolderResult listFolderResult) {
        userTokenRepository.setValue(TOKENS_HASH_KEY, userId, accessToken);
        userTokenRepository.setValue(CURSORS_HASH_KEY, userId, listFolderResult.getCursor());
    }

    private String getTokenAndCheckIsTokenExists(final String userId) throws IllegalAccessException {
        final String token = userTokenRepository.getValue(TOKENS_HASH_KEY, userId);
        if (isEmpty(token)) {
            throw new IllegalAccessException("Please allow access first.");
        }
        return token;
    }

    private void logChangedFilesOfUser(final String userId, final ListFolderResult listFolderContinue) {
        boolean hasMore = true;
        while (hasMore) {
            for (final Metadata md : listFolderContinue.getEntries()) {
                LOG.info("Changed metadata: '{}'", md);
            }
            hasMore = listFolderContinue.getHasMore();
        }
        userTokenRepository.setValue(CURSORS_HASH_KEY, userId, listFolderContinue.getCursor());
    }

}
