package com.zeldan.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.TEMPORARY_REDIRECT;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.zeldan.service.DropboxService;

/**
 * Before your app can access a Dropbox user's files, the user must authorize your application using OAuth 2. Successfully completing this authorization
 * flow gives you an access token for the user's Dropbox account, which grants you the ability to make Dropbox API calls to access their files.
 */
@RestController
@RequestMapping("/dropbox/*")
public class DropboxController {

    private final DropboxService dropboxService;

    public DropboxController(DropboxService dropboxService) {
        this.dropboxService = dropboxService;
    }

    @GetMapping("/start-auth")
    @ResponseStatus(TEMPORARY_REDIRECT)
    public void dropboxAuthStart(HttpServletRequest request,  HttpServletResponse response) throws IOException {
        String authorizePageUrl = dropboxService.startAuth(request.getSession(true));
        response.sendRedirect(authorizePageUrl);
    }

    @GetMapping("/finish-auth")
    @ResponseStatus(TEMPORARY_REDIRECT)
    public void dropboxAuthFinish(HttpServletRequest request, HttpServletResponse response) throws Exception {
        dropboxService.finishAuthAndSaveUserDetails(request.getSession(true), request.getParameterMap());
        response.setStatus(OK.value());
    }

}
