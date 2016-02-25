package com.greenowl.callisto.web.rest;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;

import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.service.ParkingActivityService;
import com.greenowl.callisto.service.ParkingValTicketStatusService;
import com.greenowl.callisto.service.SalesActivityService;
import com.greenowl.callisto.service.UserService;
import com.greenowl.callisto.web.rest.dto.ParkingActivityDTO;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.greenowl.callisto.web.rest.parking.GateOpenRequest;
import com.greenowlmobile.parkgateclient.parkgateCmdClient;
import javax.inject.Inject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.annotation.security.*;

@RestController
@RequestMapping("/api/{apiVersion}/parking")
public class GateResource {

    @Inject
    private SalesActivityService salesActivityService;

    @Inject
    private ParkingValTicketStatusService ticketStatusService;

    @Inject
    private UserService userService;
    
    @Inject
    private ParkingActivityService parkingActivityService;
    
    @RequestMapping(value = "/enter",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> enterParkingLot(@PathVariable("apiVersion") final String apiVersion, @RequestBody GateOpenRequest req) {
        User user = userService.getCurrentUser();
        if (salesActivityService.findInFlightActivityByUser(user).size() != 0) {
            return new ResponseEntity<>(genericBadReq("User already inside parking lot, can't open gate.", "/gate"),
            		org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        if (user.getPlanSubscriptions().size() > 0) {
            ParkingPlan subscribedPlan = user.getPlanSubscriptions().iterator().next().getPlanGroup();
            if (subscribedPlan != null) {
                if (subscribedPlan.getLotId() == req.getLotId()) {
                	SalesActivityDTO salesActivityDTO = salesActivityService.createParkingActivityForPlanUser(user, subscribedPlan);
                	if(salesActivityDTO!=null){
                		//open gate
	                	final String result = openGate(1, salesActivityDTO.getId().toString());
	                	//if(result1!=null && result1.indexOf("OPEN-GATE: NOT-PRESENT")>0){
	                	//	result1 = "OK Response with 33 chars:'TICKET: T12345 OPEN-GATE: OPEN OK'";										
	                	//}
	                	//final String result = result1;	                	
	                	if(result!=null && (result.contains(Constants.GATE_OPEN_RESPONSE_1) || result.contains(Constants.GATE_OPEN_RESPONSE_2))){	
	                		salesActivityService.updateParkingStatus(Constants.PARKING_STATUS_PENDING, salesActivityDTO.getId());	                		
	                		ticketStatusService.createParkingValTicketStatus(salesActivityDTO.getId(),Constants.PARKING_TICKET_TYPE_ENTER,DateTime.now());
	                		return new ResponseEntity<>(salesActivityDTO, org.springframework.http.HttpStatus.OK);
	                	}
	                	else{    
	                		salesActivityService.updateParkingStatus(Constants.PARKING_STATUS_EXCEPTION, salesActivityDTO.getId());
	                		return new ResponseEntity<>(genericBadReq("ERROR-Failed to open parking gate.", "/gate"),
	                				org.springframework.http.HttpStatus.BAD_REQUEST);
	                	}                	
                	}
                	else{
                		return new ResponseEntity<>(genericBadReq("ERROR-Failed to create parking record.", "/gate"),
                				org.springframework.http.HttpStatus.BAD_REQUEST);
                	} 
                }
                else{
                   return new ResponseEntity<>(genericBadReq("ERROR-Incorrect parking lot.", "/gate"),
                		   org.springframework.http.HttpStatus.BAD_REQUEST);
                }
            } 
            else{
                return new ResponseEntity<>(genericBadReq("ERROR-Fetching plan data.", "/gate"),
                		org.springframework.http.HttpStatus.BAD_REQUEST);
            }
        } 
        else{
            return new ResponseEntity<>(genericBadReq("ERROR-User haven't subscribed.", "/gate"),
            		org.springframework.http.HttpStatus.BAD_REQUEST);
        }        
    }
    
    
    @RequestMapping(value = "/enter2",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> enterParkingLotV2(@PathVariable("apiVersion") final String apiVersion, @RequestBody GateOpenRequest req) {
        User user = userService.getCurrentUser();
        if (parkingActivityService.findInFlightActivityByUser(user).size() != 0) {
            return new ResponseEntity<>(genericBadReq("User already inside parking lot, can't open gate.", "/gate"),
            		org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        if (user.getPlanSubscriptions().size() > 0) {
            ParkingPlan subscribedPlan = user.getPlanSubscriptions().iterator().next().getPlanGroup();
            if (subscribedPlan != null) {
                if (subscribedPlan.getLotId() == req.getLotId()) {
                	ParkingActivityDTO parkingActivityDTO = parkingActivityService.createParkingActivityForPlanUser(user, subscribedPlan);
                	if(parkingActivityDTO!=null){
                		//open gate
	                	final String result = openGate(1, parkingActivityDTO.getId().toString());
	                	//if(result1!=null && result1.indexOf("OPEN-GATE: NOT-PRESENT")>0){
	                	//	result1 = "OK Response with 33 chars:'TICKET: T12345 OPEN-GATE: OPEN OK'";										
	                	//}
	                	//final String result = result1;	                	
	                	if(result!=null && (result.contains(Constants.GATE_OPEN_RESPONSE_1) || result.contains(Constants.GATE_OPEN_RESPONSE_2))){	
	                		parkingActivityService.updateParkingStatus(Constants.PARKING_STATUS_PENDING, parkingActivityDTO.getId());	                		
	                		ticketStatusService.createParkingValTicketStatus(parkingActivityDTO.getId(),Constants.PARKING_TICKET_TYPE_ENTER,DateTime.now());
	                		return new ResponseEntity<>(parkingActivityDTO, org.springframework.http.HttpStatus.OK);
	                	}
	                	else{    
	                		parkingActivityService.updateParkingStatus(Constants.PARKING_STATUS_EXCEPTION, parkingActivityDTO.getId());
	                		return new ResponseEntity<>(genericBadReq("ERROR-Failed to open parking gate.", "/gate"),
	                				org.springframework.http.HttpStatus.BAD_REQUEST);
	                	}                	
                	}
                	else{
                		return new ResponseEntity<>(genericBadReq("ERROR-Failed to create parking record.", "/gate"),
                				org.springframework.http.HttpStatus.BAD_REQUEST);
                	} 
                }
                else{
                   return new ResponseEntity<>(genericBadReq("ERROR-Incorrect parking lot.", "/gate"),
                		   org.springframework.http.HttpStatus.BAD_REQUEST);
                }
            } 
            else{
                return new ResponseEntity<>(genericBadReq("ERROR-Fetching plan data.", "/gate"),
                		org.springframework.http.HttpStatus.BAD_REQUEST);
            }
        } 
        else{
            return new ResponseEntity<>(genericBadReq("ERROR-User haven't subscribed.", "/gate"),
            		org.springframework.http.HttpStatus.BAD_REQUEST);
        }        
    }
    
    private String openGate(int gateId, String ticketNo){
    	parkgateCmdClient parkClient = new parkgateCmdClient("localhost",2222);
    	return parkClient.openGate(gateId,ticketNo);
    }
    
    @RequestMapping(value = "/exit",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> exitParkingLot(@PathVariable("apiVersion") final String apiVersion, @RequestBody GateOpenRequest req) {
        User user = userService.getCurrentUser();
        if (salesActivityService.findInFlightActivityByUser(user).size() == 0) {
            return new ResponseEntity<>(genericBadReq("User not in the parking lot, can't open gate.", "/gate"),
            		org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        ParkingSaleActivity parkingSaleActivity = salesActivityService.findInFlightActivityByUser(user).get(0);
        if(parkingSaleActivity!=null){
        	final String result = openGate(2,Long.toString(parkingSaleActivity.getId()));
        	//if(result1!=null && result1.indexOf("OPEN-GATE: NOT-PRESENT")>0){
        	//	result1 = "OK Response with 33 chars:'TICKET: T12345 OPEN-GATE: OPEN OK'";										
        	//}
        	//final String result = result1;
        	if(result!=null && (result.contains(Constants.GATE_OPEN_RESPONSE_1) || result.contains(Constants.GATE_OPEN_RESPONSE_2))){
        		salesActivityService.updateParkingStatus(Constants.PARKING_STATUS_PENDING, parkingSaleActivity.getId());
        		ticketStatusService.createParkingValTicketStatus(parkingSaleActivity.getId(),Constants.PARKING_TICKET_TYPE_EXIT,DateTime.now());
        		SalesActivityDTO salesActivityDTO = salesActivityService.contructDTO(parkingSaleActivity, user);
        		return new ResponseEntity<>(salesActivityDTO, org.springframework.http.HttpStatus.OK);
        	}        	
        	else{
        		salesActivityService.updateParkingStatus(Constants.PARKING_STATUS_EXCEPTION, parkingSaleActivity.getId());
        		return new ResponseEntity<>(genericBadReq("ERROR-Failed to open parking gate.", "/gate"),
        				org.springframework.http.HttpStatus.BAD_REQUEST);
        	}
        }
        else{
        	return new ResponseEntity<>(genericBadReq("ERROR-failed to find parking record.", "/gate"),
            		org.springframework.http.HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/systemOpen",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> systemOpenEnterGate(@PathVariable("apiVersion") final String apiVersion, @RequestBody GateOpenRequest req, @RequestParam final String reason) {
        User user = userService.getCurrentUser();
        ParkingSaleActivity parkingSaleActivity = new ParkingSaleActivity();
        parkingSaleActivity.setLotId(req.getLotId());
        parkingSaleActivity.setActivityHolder(user);
        parkingSaleActivity.setUserEmail(user.getLogin());
        parkingSaleActivity.setParkingStatus("System");
        parkingSaleActivity.setExceptionFlag(reason);
        //Gate Open
        //salesActivityRepository.save(parkingSaleActivity);
        SalesActivityDTO salesActivityDTO = salesActivityService.contructDTO(parkingSaleActivity, user);
        return new ResponseEntity<>(salesActivityDTO, org.springframework.http.HttpStatus.OK);
    }
}

