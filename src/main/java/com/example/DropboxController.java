package com.example;

import static org.springframework.http.HttpStatus.TEMPORARY_REDIRECT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Before your app can access a Dropbox user's files, the user must authorize your application using OAuth 2. Successfully completing this authorization
 * flow gives you an access token for the user's Dropbox account, which grants you the ability to make Dropbox API calls to access their files.
 */
@RestController
@RequestMapping("/dropbox/*")
public class DropboxController {

    private static final Logger LOG = LoggerFactory.getLogger(DropboxController.class);

    @RequestMapping(value = "/authorize", method = GET)
    @ResponseStatus(TEMPORARY_REDIRECT)
    public void authorize(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {
    }

    @RequestMapping(value = "/access", method = GET)
    @ResponseStatus(TEMPORARY_REDIRECT)
    public void access(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {
    }
}
