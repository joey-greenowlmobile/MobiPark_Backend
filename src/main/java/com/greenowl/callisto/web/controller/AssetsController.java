package com.greenowl.callisto.web.controller;

import com.greenowl.callisto.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

@Controller
@RequestMapping("/assets")
public class AssetsController {

    private static final Logger LOG = LoggerFactory.getLogger(AssetsController.class);

    @Inject
    private FileService fileService;


}
