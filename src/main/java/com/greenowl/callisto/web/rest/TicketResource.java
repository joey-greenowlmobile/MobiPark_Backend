package com.greenowl.callisto.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.exception.ErrorResponseFactory;
import com.greenowl.callisto.repository.ParkingActivityRepository;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.web.rest.dto.TicketStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;



@RestController
@RequestMapping("/api/{apiVersion}/ticket")
@RolesAllowed(AuthoritiesConstants.TICKET)
public class TicketResource {

	private static final Logger LOG = LoggerFactory.getLogger(TicketResource.class);
	
	
	@Inject
    private ParkingActivityRepository parkingActivityRepository;
	
	@RequestMapping(value = "/status", method = {RequestMethod.GET, RequestMethod.POST}
    , produces = MediaType.APPLICATION_JSON_VALUE)
	@RolesAllowed(AuthoritiesConstants.TICKET)
	public ResponseEntity<?> updateTicketStatus(@PathVariable("apiVersion") final String apiVersion,@RequestParam(required = true) final Long ticketNo, @RequestParam(required = true) final String accessDateTime,
                                       @RequestParam(required = true) final String status,@RequestParam(required = true) final String gateId,
                                       @RequestParam(required = false) String format) {
		ParkingActivity activity = parkingActivityRepository.findOne(ticketNo);
        if (activity == null) {
            return new ResponseEntity<>(ErrorResponseFactory.genericBadReq("Unable to find ticket with id = " + ticketNo,"/api/"+apiVersion+"/ticket/status/" + ticketNo,204), org.springframework.http.HttpStatus.BAD_REQUEST);
        }
		//to do
        
	    TicketStatusDTO ticketStatusDTO = new TicketStatusDTO(ticketNo, accessDateTime,status,"OK");
		return new ResponseEntity(ticketStatusDTO,org.springframework.http.HttpStatus.OK);
		
	}
	
	
	
	
	
}
