package com.greenowl.callisto.web.rest.admin;

import com.greenowl.callisto.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/admin")
@RolesAllowed(AuthoritiesConstants.ADMIN)
public class GeneralAdminResource {

    private static final Logger LOG = LoggerFactory.getLogger(GeneralAdminResource.class);


}
