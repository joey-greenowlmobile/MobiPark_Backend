package com.greenowl.callisto.web.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.domain.GateStatus;
import com.greenowl.callisto.repository.GateStatusRepository;
import org.joda.time.DateTime;
import javax.annotation.security.RolesAllowed;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/{apiVersion}/gate")
@RolesAllowed(AuthoritiesConstants.TICKET)
public class GateHealthResource {

	private static final Logger LOG = LoggerFactory.getLogger(GateHealthResource.class);
	
	@Inject
	private GateStatusRepository gateStatusRepository;
	
	@RequestMapping(value = "/status", method = {RequestMethod.GET, RequestMethod.POST}
    , produces = MediaType.APPLICATION_JSON_VALUE)
	@RolesAllowed(AuthoritiesConstants.TICKET)
	public ResponseEntity<?> reportGateStatus(@PathVariable("apiVersion") final String apiVersion,@RequestParam(required = true) final String status,@RequestParam(required = true) final String gateId) {
		GateStatus gateStatus = new GateStatus();
		gateStatus.setGateId(gateId);
 		gateStatus.setStatus(status);
		gateStatus.setCheckTime(DateTime.now());
		try{			
		    gateStatusRepository.save(gateStatus);
		}
		catch(Exception e){
			LOG.error(e.getMessage(), e);
		}
		return new ResponseEntity<>(org.springframework.http.HttpStatus.OK);
	}
	
	
	
}
