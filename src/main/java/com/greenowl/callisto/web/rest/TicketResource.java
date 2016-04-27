package com.greenowl.callisto.web.rest;

import org.joda.time.DateTimeZone;
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
import com.greenowl.callisto.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import java.net.URLDecoder;
import java.text.*;
import java.util.*;
import org.joda.time.DateTime;


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
                                       @RequestParam(required = false) String message) {
		ParkingActivity activity = parkingActivityRepository.getParkingActivityById(ticketNo);		
        if (activity == null) {        	
            return new ResponseEntity<>(ErrorResponseFactory.genericBadReq("Unable to find ticket with id = " + ticketNo,"/api/"+apiVersion+"/ticket/status",204), org.springframework.http.HttpStatus.BAD_REQUEST);
        }
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = null;
        try{
        	time = sdf.parse(accessDateTime);        	
        }
        catch(Exception e){
        	return new ResponseEntity<>(ErrorResponseFactory.genericBadReq("Bad accessDateTime parameter:" + accessDateTime,"/api/"+apiVersion+"/ticket/status",400), org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        int gId = 0;
        try{
        	gId = Integer.parseInt(gateId);
        }
        catch(Exception e){
        	LOG.error("Wrong gate id:"+gateId,e);
        }
        if(Constants.PARKING_TICKET_TYPE_ENTER==gId){
        	if(activity.getEntryDatetime()==null){
	        	activity.setEntryDatetime(new DateTime(Calendar.getInstance().getTime(),DateTimeZone.UTC));        	
	        	if(status!=null && status.toLowerCase().contains("pass")){  
	        		if(Constants.PARKING_STATUS_PENDING_ENTER_MANUAL.equals(activity.getParkingStatus())){
	        			activity.setParkingStatus(Constants.PARKING_STATUS_IN_FLIGHT_MANUAL);
	        		}
	        		else{
	        			activity.setParkingStatus(Constants.PARKING_STATUS_IN_FLIGHT);
	        		}
	        	}
	        	else{
	        		if(Constants.PARKING_STATUS_PENDING_ENTER_MANUAL.equals(activity.getParkingStatus())){
	        			activity.setParkingStatus(Constants.PARKING_STATUS_EXCEPTION_ENTER_MANUAL);
	        		}
	        		else{
	        			activity.setParkingStatus(Constants.PARKING_STATUS_EXCEPTION_ENTER);
	        		}
	        	}
        	}
        	else{
        		LOG.error("EntryDateTime is not empty, ticket no:"+ticketNo);
        	}
        }
        else if(Constants.PARKING_TICKET_TYPE_EXIT==gId){
        	if(activity.getExitDatetime()==null){
	            activity.setExitDatetime(new DateTime(Calendar.getInstance().getTime(), DateTimeZone.UTC));	            
	            if(status!=null && status.toLowerCase().contains("pass")){            	
	        		if(Constants.PARKING_STATUS_PENDING_EXIT_MANUAL.equals(activity.getParkingStatus())){
	        			activity.setParkingStatus(Constants.PARKING_STATUS_COMPLETED_MANUAL);
	        		}
	        		else{
	        			activity.setParkingStatus(Constants.PARKING_STATUS_COMPLETED);
	        		}            	
	        	}
	        	else{
	        		if(Constants.PARKING_STATUS_PENDING_EXIT_MANUAL.equals(activity.getParkingStatus())){
	        			activity.setParkingStatus(Constants.PARKING_STATUS_EXCEPTION_EXIT_MANUAL);
	        		}
	        		else{
	        			activity.setParkingStatus(Constants.PARKING_STATUS_EXCEPTION_EXIT);
	        		}
	        	}
        	}
        	else{
        		LOG.error("ExitDateTime is not empty, ticket no:"+ticketNo);
        	}
        }
        else{
        	return new ResponseEntity<>(ErrorResponseFactory.genericBadReq("Bad gateId parameter:" + gateId,"/api/"+apiVersion+"/ticket/status",400), org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        if(message!=null){
        	try{
        		activity.setGateResponse((activity.getGateResponse()==null?"":(activity.getGateResponse()+";"))+sdf2.format(Calendar.getInstance().getTime())+" "+status+", "+URLDecoder.decode(message,"utf-8"));
        	}
        	catch(Exception e){
        		LOG.error(e.getMessage(),e);
        		activity.setGateResponse((activity.getGateResponse()==null?"":(activity.getGateResponse()+";"))+sdf2.format(Calendar.getInstance().getTime())+" "+status+", "+message);
        	}
        }
        else{
        	activity.setGateResponse((activity.getGateResponse()==null?"":(activity.getGateResponse()+";"))+sdf2.format(Calendar.getInstance().getTime())+" "+status);
        }
        activity.setExceptionFlag(((activity.getExceptionFlag()==null || activity.getExceptionFlag().trim().length()==0)?"":(activity.getExceptionFlag()+","))+sdf2.format(time)+" "+activity.getParkingStatus());
        try{
            parkingActivityRepository.save(activity);
		    TicketStatusDTO ticketStatusDTO = new TicketStatusDTO(ticketNo, accessDateTime,status,"OK");
			return new ResponseEntity(ticketStatusDTO,org.springframework.http.HttpStatus.OK);
        }
        catch(Exception e){
        	LOG.error("failed to update parkingActivity",e);
        	return new ResponseEntity<>(ErrorResponseFactory.genericBadReq("Failed to update parkingActivity","/api/"+apiVersion+"/ticket/status",500), org.springframework.http.HttpStatus.BAD_REQUEST);
        }
	}
	
	
	
	
	
}
