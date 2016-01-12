package com.greenowl.callisto.util;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.OK;

public class FileUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Writes an amazon S3Objects byte contents to a HttpServletResponse and sets all corresponding headers.
     * Will return a NO_CONTENT Header if the content is empty. IF a null object is passed will not make any assumptions
     * about response. I.e will not return 404.
     *
     * @param response
     * @param s3Object
     * @return
     */
    public static ResponseEntity responseFile(HttpServletResponse response, S3Object s3Object) {
        try (ServletOutputStream stream = response.getOutputStream()) {
            byte[] bytes = IOUtils.toByteArray(s3Object.getObjectContent());
            if (bytes == null || bytes.length == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); //No File pulled from File system
            }
            ObjectMetadata info = s3Object.getObjectMetadata();
            String fileName = getFileName(s3Object.getKey());
            LOG.debug("returning file with filename = {}", info.getContentDisposition());
            LOG.debug("returning file with Content type for file =  {}", info.getContentType());
            response.setContentType(info.getContentType());
            response.addHeader(CONTENT_DISPOSITION, "attachment; filename="
                    + fileName); //allow client to download
            response.setContentLength(bytes.length);
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            LOG.error("Unable to download object from S3");
            return new ResponseEntity<>(EXPECTATION_FAILED); //Error during stream
        }
        return new ResponseEntity<>(OK); //All good.
    }


    private static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return "file";
        }
        String[] splitFilePath = filePath.split("/");
        return splitFilePath[splitFilePath.length - 1];
    }
}
