package com.greenowl.callisto.config;

import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.security.Http401UnauthorizedEntryPoint;
import com.greenowl.callisto.security.password.PasswordResetTokenProvider;
import com.greenowl.callisto.security.xauth.TokenProvider;
import com.greenowl.callisto.security.xauth.XAuthTokenConfigurer;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements EnvironmentAware {

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private TokenProvider tokenProvider;

    private RelaxedPropertyResolver propertyResolver;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/scripts/**/*.{js,html}")
                .antMatchers("favicon.ico")
                .antMatchers("/bower_components/**")
                .antMatchers("/i18n/**")
                .antMatchers("/assets/**")
                .antMatchers("/swagger-ui/index.html")
                .antMatchers("/test/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/v2/api-docs/**").permitAll() // Swagger Documentation
                .antMatchers("/reset-password/**").permitAll()   // Password Reset
                .antMatchers("/api/**/register").permitAll()   // Register a new User
                .antMatchers("/api/**/authenticate/**").permitAll()  // authenticate a user
                .antMatchers("/api/media/**").permitAll()  // Let any user download content
                .antMatchers("/api/logs/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/pub/**").permitAll()
                .antMatchers("/api/**").authenticated()
                //Developer related end points for debugging/monitoring
                .antMatchers("/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/health/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/dump/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/shutdown/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/beans/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/configprops/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/info/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/autoconfig/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/env/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/protected/**").authenticated()
                .antMatchers("/ticket/**").hasAuthority(AuthoritiesConstants.TICKET)
                .and()
                .apply(securityConfigurerAdapter());

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "authentication.");
    }

    private XAuthTokenConfigurer securityConfigurerAdapter() {
        return new XAuthTokenConfigurer(userDetailsService, tokenProvider);
    }

    @Bean
    public PasswordResetTokenProvider passwordResetTokenProvider() {
        String secret = propertyResolver.getProperty("password.reset.token", String.class, "1259smfULaz");
        int validityInSeconds = propertyResolver.getProperty("password.reset.tokenValidityInSeconds", Integer.class, 3600);
        return new PasswordResetTokenProvider(secret, validityInSeconds);
    }


}
