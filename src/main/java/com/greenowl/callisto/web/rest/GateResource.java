package com.greenowl.callisto.web.rest;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;

import java.util.Calendar;

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
import com.greenowl.callisto.domain.ParkingValTicketStatus;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.service.ParkingValTicketStatusService;
import com.greenowl.callisto.service.SalesActivityService;
import com.greenowl.callisto.service.UserService;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.greenowl.callisto.web.rest.parking.GateOpenRequest;
import com.greenowlmobile.parkgateclient.parkgateCmdClient;


@RestController
@RequestMapping("/api/{apiVersion}/parking")
public class GateResource {

    @Inject
    private SalesActivityService salesActivityService;

    @Inject
    private ParkingValTicketStatusService ticketStatusService;

    @Inject
    private UserService userService;
    
    
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
                	SalesActivityDTO salesActivityDTO = salesActivityService.createSaleActivityForPlanUser(user, subscribedPlan);
                	if(salesActivityDTO!=null){
                		//open gate
	                	parkgateCmdClient parkClient = new parkgateCmdClient("localhost",2222);
	                	String result1 = parkClient.openGate(Constants.ENTER_GATE, salesActivityDTO.getId().toString());
	                	//if(result1!=null && result1.indexOf("OPEN-GATE: NOT-PRESENT")>0){
	                	//	result1 = "OK Response with 33 chars:'TICKET: T12345 OPEN-GATE: OPEN OK'";										
	                	//}
	                	final String result = result1;
	                	
	                	if(result!=null && result.startsWith("OK")){	
	                		salesActivityService.updateParkingStatus(Constants.PARKING_STATUS_PENDING, salesActivityDTO.getId());
	                		ParkingValTicketStatus pvts = new ParkingValTicketStatus();
	                		pvts.setId(salesActivityDTO.getId());
	                		pvts.setCreatedDateTime(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
	                		ticketStatusService.createParkingValTicketStatus(pvts);
	                		return new ResponseEntity<>(salesActivityDTO, org.springframework.http.HttpStatus.OK);
	                	}
	                	else if(result==null || result.startsWith("ERROR")){    		
	                		return new ResponseEntity<>(genericBadReq("ERROR-Failed to open parking gate.", "/gate"),
	                				org.springframework.http.HttpStatus.BAD_REQUEST);
	                	}          	    
	                	
                	}
                	else{
                		return new ResponseEntity<>(genericBadReq("ERROR-Failed to create parking record.", "/gate"),
                				org.springframework.http.HttpStatus.BAD_REQUEST);
                	}                  

                } else {
                   return new ResponseEntity<>(genericBadReq("ERROR-Incorrect parking lot.", "/gate"),
                		   org.springframework.http.HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(genericBadReq("ERROR-Fetching plan data.", "/gate"),
                		org.springframework.http.HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(genericBadReq("ERROR-User haven't subscribed.", "/gate"),
            		org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(genericBadReq("ERROR-", "/gate"),
        		org.springframework.http.HttpStatus.BAD_REQUEST);
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
        salesActivityService.updateExitTime(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()), parkingSaleActivity.getId());      
        //closeGate Function
        salesActivityService.updateParkingStatus("Finished parking", parkingSaleActivity.getId());
        SalesActivityDTO salesActivityDTO = salesActivityService.contructDTO(parkingSaleActivity, user);
        return new ResponseEntity<>(salesActivityDTO, org.springframework.http.HttpStatus.OK);
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

