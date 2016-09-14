package com.example.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.TEMPORARY_REDIRECT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.DropboxService;

/**
 * Before your app can access a Dropbox user's files, the user must authorize your application using OAuth 2. Successfully completing this authorization
 * flow gives you an access token for the user's Dropbox account, which grants you the ability to make Dropbox API calls to access their files.
 */
@RestController
@RequestMapping("/dropbox/*")
public class DropboxController {

    @Autowired
    private DropboxService dropboxService;

    @RequestMapping(value = "/start-auth", method = GET)
    @ResponseStatus(TEMPORARY_REDIRECT)
    public void dropboxAuthStart(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String authorizePageUrl = dropboxService.startAuth(request.getSession(true));
        response.sendRedirect(authorizePageUrl);
    }

    @RequestMapping(value = "/finish-auth", method = GET)
    @ResponseStatus(TEMPORARY_REDIRECT)
    public void dropboxAuthFinish(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        dropboxService.finishAuthAndSaveUserDetails(request.getSession(true), request.getParameterMap());
        response.setStatus(OK.value());
    }

}
