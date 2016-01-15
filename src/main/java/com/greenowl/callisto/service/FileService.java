package com.greenowl.callisto.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.Optional;

/**
 * Wrapper around file storage implementation used.
 */
public class FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

    @Inject
    private TransferManager transferManager;

    @Inject
    private AmazonS3Client s3Client;

    private String bucket;

    public FileService() {
    }

    public FileService(String bucket) {
        this.bucket = bucket;
    }

    /**
     * Uploads a file InputStream for storage.
     *
     * @param filePath
     * @param inputStream
     * @param info
     */
    @Async
    public void upload(String filePath, InputStream inputStream, ObjectMetadata info) {
        transferManager.upload(bucket, filePath, inputStream, info);
    }

    @Timed
    public S3Object getFile(String relativePath) {
        LOG.debug("Checking for S3 File with path = {}", relativePath);
        return s3Client.getObject(new GetObjectRequest(bucket, relativePath));
    }

    public Optional<S3Object> getFileOptional(String relativePath) {
        LOG.debug("Checking for S3 File with path = {}", relativePath);
        S3Object obj = s3Client.getObject(new GetObjectRequest(bucket, relativePath));
        return (obj == null) ? Optional.empty() : Optional.of(obj);
    }


}
