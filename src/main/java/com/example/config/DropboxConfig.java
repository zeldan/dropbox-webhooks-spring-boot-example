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

    @Value("${spring.application.name}")
    private String appName;

    private final DropboxConfigProperties dropboxConfigProperties;

    public DropboxConfig(final DropboxConfigProperties dropboxConfigProperties) {
        this.dropboxConfigProperties = dropboxConfigProperties;
    }

    @Bean
    public DbxRequestConfig dbxRequestConfig() {
        return new DbxRequestConfig(appName);
    }

    @Bean
    public DbxWebAuth dbxWebAuth(final DbxRequestConfig requestConfig) {
        final DbxAppInfo appInfo = new DbxAppInfo(dropboxConfigProperties.getKey(), dropboxConfigProperties.getSecret());
        return new DbxWebAuth(requestConfig, appInfo);
    }
}
