package com.zeldan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;

@Configuration
public class DropboxConfig {

    private final String appName;
    private final DropboxConfigProperties dropboxConfigProperties;

    public DropboxConfig(DropboxConfigProperties dropboxConfigProperties,
                         @Value("${spring.application.name}") String appName) {
        this.dropboxConfigProperties = dropboxConfigProperties;
        this.appName = appName;
    }

    @Bean
    public DbxRequestConfig dbxRequestConfig() {
        return new DbxRequestConfig(appName);
    }

    @Bean
    public DbxWebAuth dbxWebAuth(DbxRequestConfig requestConfig) {
        DbxAppInfo appInfo = new DbxAppInfo(dropboxConfigProperties.getKey(), dropboxConfigProperties.getSecret());
        return new DbxWebAuth(requestConfig, appInfo);
    }
}
