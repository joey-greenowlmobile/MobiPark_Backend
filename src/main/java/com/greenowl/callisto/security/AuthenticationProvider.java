package com.greenowl.callisto.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This is a Bean managed by the Spring container responsible for authenticating users through DEFAULT username/password
 * method and setting the global SecurityContext. Please do not make any changes unless you have a firm understanding of
 * basic security flow and how Spring Security works.
 * Refer to the following documentation:
 * http://docs.spring.io/spring-security/site/docs/4.0.1.RELEASE/reference/htmlsingle/
 */
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    private final Logger log = LoggerFactory.getLogger(AuthenticationProvider.class);

    private PasswordEncoder passwordEncoder;

    private UserDetailsService userDetailsService;

    public AuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken token =
                (UsernamePasswordAuthenticationToken) authentication;
        String login = token.getName();
        UserDetails user = userDetailsService.loadUserByUsername(login);
        if (user == null) {
            throw new UsernameNotFoundException("User does not exists");
        }
        String password = user.getPassword(); //encrypted password
        String tokenPassword = (String) token.getCredentials(); //Unencrypted password
        if (!passwordEncoder.matches(tokenPassword, password)) {
            throw new BadCredentialsException("Invalid username/password");
        }
        return new UsernamePasswordAuthenticationToken(user, password,
                user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken
                .class.equals(authentication);
    }
}
