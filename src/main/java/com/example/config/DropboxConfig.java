package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;

/**
 * Configuration class for dropbox settings.
 */
@Configuration
public class DropboxConfig {

    @Value("${app.name}")
    private String appName;

    @Value("${dropbox.app.key}")
    private String appKey;

    @Value("${dropbox.app.secret}")
    private String appSecret;

    @Bean
    public DbxRequestConfig dbxRequestConfig() {
        return new DbxRequestConfig(appName);
    }

    @Bean
    public DbxWebAuth dbxWebAuth(final DbxRequestConfig requestConfig) {
        final DbxAppInfo appInfo = new DbxAppInfo(appKey, appSecret);
        return new DbxWebAuth(requestConfig, appInfo);
    }
}
