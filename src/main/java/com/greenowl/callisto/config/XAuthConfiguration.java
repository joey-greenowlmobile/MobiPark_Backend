package com.greenowl.callisto.config;

import com.greenowl.callisto.security.xauth.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by ahmed aly 2015-03-27
 * Configures x-auth-token security.
 */
@Configuration
public class XAuthConfiguration implements EnvironmentAware {

    private RelaxedPropertyResolver propertyResolver;

    private static final Logger LOG = LoggerFactory.getLogger(XAuthConfiguration.class);

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "authentication.xauth.");
    }

    @Bean
    public TokenProvider tokenProvider() {
        String secret = propertyResolver.getProperty("secret", String.class, "mySecretXAuthSecret");
        int validityInSeconds = propertyResolver.getProperty("tokenValidityInSeconds", Integer.class, 3600);
        LOG.debug("Setting token life span to {} seconds", validityInSeconds);
        return new TokenProvider(secret, validityInSeconds);
    }
}
