package com.greenowl.callisto.web.rest.admin;

import com.greenowl.callisto.domain.AppConfig;
import com.greenowl.callisto.repository.AppConfigRepository;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.service.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RolesAllowed(AuthoritiesConstants.ADMIN)
public class GeneralAdminResource {

    private static final Logger LOG = LoggerFactory.getLogger(GeneralAdminResource.class);

    @Inject
    private ConfigService configService;

    @Inject
    private AppConfigRepository appConfigRepository;


    @RequestMapping(value = "/configs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> configMap() {
        Map<String, String> map = configService.getMap();
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value = "/config",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> newConfig(@RequestParam final String key, @RequestParam final String value, @RequestParam(defaultValue = "java.lang.String") String type) {
        AppConfig config = new AppConfig();
        config.setKey(key);
        config.setType(type);
        config.setValue(value);
        AppConfig savedConfig = appConfigRepository.save(config);
        return new ResponseEntity<>(savedConfig, HttpStatus.OK);
    }

}
