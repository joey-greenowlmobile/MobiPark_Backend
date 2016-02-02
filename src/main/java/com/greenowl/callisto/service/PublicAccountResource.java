package com.greenowl.callisto.service;


import com.greenowl.callisto.util.ApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static org.springframework.http.HttpStatus.OK;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/pub/{apiVersion}/")
public class PublicAccountResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublicAccountResource.class);

    @Inject
    private PasswordResetService passwordResetService;


    /**
     * POST  /account/password/reset -> changes the current user's password
     */
    @RequestMapping(value = "/test",
            method = {RequestMethod.GET},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> test(@PathVariable("apiVersion") final String apiVersion) {
        String msg = "Your version =" + ApiUtil.getVersion();
        return new ResponseEntity<>(msg, OK);
    }

}
