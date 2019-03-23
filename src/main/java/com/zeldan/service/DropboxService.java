package com.zeldan.service;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.zeldan.config.DropboxConfigProperties;
import com.zeldan.repository.UserTokenRepository;

/**
 * Use this class to make remote calls to the Dropbox API user endpoints.
 * You'll need an access token first, normally acquired by directing a Dropbox user through the auth flow using DbxWebAuth.
 */
@Service
public class DropboxService {

    private static final Logger LOG = getLogger(DropboxService.class);

    private static final String CURSORS_HASH_KEY = "cursors";
    private static final String TOKENS_HASH_KEY = "tokens";

    private final DropboxConfigProperties dropboxConfigProp;
    private final DbxWebAuth auth;
    private final DbxRequestConfig requestConfig;
    private final UserTokenRepository userTokenRepository;

    public DropboxService(DropboxConfigProperties dropboxConfigProperties,
                          DbxWebAuth auth,
                          DbxRequestConfig requestConfig,
                          UserTokenRepository userTokenRepository) {
        this.dropboxConfigProp = dropboxConfigProperties;
        this.auth = auth;
        this.requestConfig = requestConfig;
        this.userTokenRepository = userTokenRepository;
    }

    public void logChangedFiles(String userId) throws Exception {
        String accessToken = getTokenAndCheckIsTokenExists(userId);
        DbxClientV2 client = new DbxClientV2(requestConfig, accessToken);
        String cursor = userTokenRepository.getValue(CURSORS_HASH_KEY, userId);
        ListFolderResult listFolderContinue = client.files().listFolderContinue(cursor);
        logChangedFilesOfUser(userId, listFolderContinue);
    }

    public String startAuth(HttpSession session) {
        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, dropboxConfigProp.getSessionStore().getKey());
        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
                .withRedirectUri(dropboxConfigProp.getRedirectUri(), csrfTokenStore)
                .build();
        return auth.authorize(authRequest);
    }

    public void finishAuthAndSaveUserDetails(HttpSession session, Map<String, String[]> parameterMap) throws Exception {
        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, dropboxConfigProp.getSessionStore().getKey());
        DbxAuthFinish authFinish = auth.finishFromRedirect(dropboxConfigProp.getRedirectUri(), csrfTokenStore, parameterMap);
        String accessToken = authFinish.getAccessToken();
        DbxClientV2 client = new DbxClientV2(requestConfig, accessToken);
        String userId = authFinish.getUserId();
        ListFolderResult listFolderResult = client.files().listFolderBuilder("").withRecursive(true).start();
        saveAccessTokenAndActualCursor(userId, accessToken, listFolderResult);
    }

    private void saveAccessTokenAndActualCursor(String userId, String accessToken, ListFolderResult listFolderResult) {
        userTokenRepository.setValue(TOKENS_HASH_KEY, userId, accessToken);
        userTokenRepository.setValue(CURSORS_HASH_KEY, userId, listFolderResult.getCursor());
    }

    private String getTokenAndCheckIsTokenExists(String userId) throws IllegalAccessException {
        String token = userTokenRepository.getValue(TOKENS_HASH_KEY, userId);
        if (isEmpty(token)) {
            throw new IllegalAccessException("Please allow access first.");
        }
        return token;
    }

    private void logChangedFilesOfUser(String userId, ListFolderResult listFolderContinue) {
        boolean hasMore = true;
        while (hasMore) {
            for (Metadata md : listFolderContinue.getEntries()) {
                LOG.info("Changed metadata: '{}'", md);
            }
            hasMore = listFolderContinue.getHasMore();
        }
        userTokenRepository.setValue(CURSORS_HASH_KEY, userId, listFolderContinue.getCursor());
    }

}
