package com.greenowl.callisto.web.rest;

import com.amazonaws.services.s3.model.S3Object;
import com.greenowl.callisto.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/{apiVersion}")
public class TransactionResource {

    @Inject
    private FileService fileService;

    @RequestMapping(value = "/transactions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> enterParkingLot(@PathVariable("apiVersion") final String apiVersion) {
        S3Object s3Object = fileService.getFile("MOCK_DATA.json");

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
