package com.greenowl.callisto.web.rest;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.service.FileService;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.OK;


/**
 * Maintains the runtime configuration file responsible for managing client configurations at runtime.
 */
@RestController
@RequestMapping("/pub")
public class PublicRuntimeConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublicRuntimeConfigResource.class);

    @Inject
    private FileService fileService;

    /**
     * GET -> /rule Get the profile picture for currently logged in user.
     */
    @RequestMapping(value = "/runtime_config", produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public ResponseEntity<?> getRuntimeConfig(HttpServletResponse response) {
        String filePath = Constants.RUNTIME_CONFIG_FILE_PATH;
        LOG.debug("Attempting to obtain runtime configuration file from S3");
        S3Object s3Object = fileService.getFile(filePath);
        try (ServletOutputStream stream = response.getOutputStream()) {
            byte[] bytes = IOUtils.toByteArray(s3Object.getObjectContent());
            ObjectMetadata info = s3Object.getObjectMetadata();
            LOG.debug("returning file with filename = {}", info.getContentDisposition());
            LOG.debug("returning file with Content type for file =  {}", info.getContentType());
            response.setContentType(info.getContentType());
            response.addHeader(CONTENT_DISPOSITION, "attachment; filename="
                    + info.getContentDisposition()); //allow client to download
            response.setContentLength(bytes.length);
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            LOG.error("Unable to download object from S3");
            return new ResponseEntity<>(EXPECTATION_FAILED); //Error during stream
        }
        return new ResponseEntity<>(OK); //All good.
    }

    /**
     * GET -> /rule Get the profile picture for currently logged in user.
     */
    @RequestMapping(value = "/runtime_configs", produces = MediaType.TEXT_PLAIN_VALUE,
            method = RequestMethod.GET)
    public ResponseEntity<?> getRules() throws JSONException {
        String filePath = Constants.RUNTIME_CONFIG_FILE_PATH;
        LOG.debug("Attempting to save new file to Amazon s3");
        S3Object s3Object = fileService.getFile(filePath);
        String text
                = getText(s3Object);
        return new ResponseEntity<>(text, OK); //All good.
    }

    private String getText(S3Object object) {
        S3ObjectInputStream stream = object.getObjectContent();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String text = "";
        String temp;
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                text += temp;
            }
            bufferedReader.close();
            stream.close();
        } catch (IOException e) {
            LOG.error("Exception while reading the string {}", e);
        }
        return text;
    }

}
