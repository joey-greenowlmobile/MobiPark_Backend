package com.greenowl.callisto.config.cloud;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.greenowl.callisto.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration class responsible for defining any possible beans responsible for interfacing with
 * cloud provider. Currently the primary Cloud Service used is AWS.
 */
@Configuration
public class CloudConfiguration implements EnvironmentAware {

    private RelaxedPropertyResolver propertyResolver;

    private Environment env;

    private static final Logger LOG = LoggerFactory.getLogger(CloudConfiguration.class);

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
        this.propertyResolver = new RelaxedPropertyResolver(env);
    }

    @Bean
    public AmazonS3Client s3Client(AWSCredentials credentials) {
        return new AmazonS3Client(credentials);
    }

    @Bean
    public TransferManager transferManager(AWSCredentials credentials) {
        return new TransferManager(credentials);
    }

    @Bean
    public AWSCredentials awsc() {
        return new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return propertyResolver.getProperty("aws.credentials.id");
            }

            @Override
            public String getAWSSecretKey() {
                return propertyResolver.getProperty("aws.credentials.secret");
            }
        };
    }

    @Bean
    public FileService fileService() {
        String bucket = propertyResolver.getProperty("cloud.aws.s3.bucket", String.class, "www.callisto-staging.com");
        LOG.info("Creating file service with bucket = {}", bucket);
        return new FileService(bucket);
    }

}
