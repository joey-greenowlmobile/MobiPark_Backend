package com.greenowl.callisto.config.apidoc;

import com.greenowl.callisto.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@Profile("!" + Constants.SPRING_PROFILE_FAST)
public class SwaggerConfiguration {

    private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

    public static final String DEFAULT_INCLUDE_PATTERN = "/(api|pub)/.*";

    /**
     * Swagger configuration.
     */
    @Bean
    public Docket swaggerSpringfoxDocket(com.greenowl.callisto.config.CallistoBeanConfigurationProperties props) {
        log.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();
        ApiInfo apiInfo = new ApiInfo(
                props.getSwagger().getTitle(),
                props.getSwagger().getDescription(),
                props.getSwagger().getVersion(),
                props.getSwagger().getTermsOfServiceUrl(),
                props.getSwagger().getContact(),
                props.getSwagger().getLicense(),
                props.getSwagger().getLicenseUrl());

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .genericModelSubstitutes(ResponseEntity.class)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .directModelSubstitute(org.joda.time.DateTime.class, String.class)
                .directModelSubstitute(org.joda.time.LocalDate.class, String.class)
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .build();
        watch.stop();
        log.debug("Initialized Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }
}
