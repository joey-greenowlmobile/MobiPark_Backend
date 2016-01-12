package com.greenowl.callisto.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.greenowl.callisto.security.xauth.Token;
import com.greenowl.callisto.security.xauth.TokenProvider;
import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@SuppressWarnings("SpringJavaAutowiringInspection")
@RestController
@RequestMapping("/api/{apiVersion}/")
// please don't change this flow unless you understand the security configuration used.
public class UserXAuthTokenController {

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private UserDetailsService userDetailsService;


    private static final Logger LOG = LoggerFactory.getLogger(UserXAuthTokenController.class);

    @RequestMapping(value = "/authenticate",
            method = {RequestMethod.POST, RequestMethod.GET})
    @Timed
    public Token authenticate(@PathVariable(value = "apiVersion") final String apiVersion,
                              @Email @RequestParam(required = false) String username,
                              @RequestParam String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = this.authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails details = this.userDetailsService.loadUserByUsername(username);
        return tokenProvider.createToken(details);
    }

}
