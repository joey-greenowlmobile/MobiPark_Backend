package com.greenowl.callisto.service;


import com.greenowl.callisto.exception.ErrorResponseFactory;
import com.greenowl.callisto.service.util.UserUtil;
import com.greenowl.callisto.util.ApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

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
