package com.greenowl.callisto.config;

import com.greenowl.callisto.aop.logging.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile({ Constants.SPRING_PROFILE_DEVELOPMENT, Constants.SPRING_PROFILE_STAGING })
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
