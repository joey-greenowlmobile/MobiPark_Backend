package com.greenowl.callisto.web.rest;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.SalesActivityRepository;
import com.greenowl.callisto.service.SalesActivityService;
import com.greenowl.callisto.service.UserService;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.greenowl.callisto.web.rest.parking.GateOpenRequest;

@RestController
@RequestMapping("/api/{apiVersion}/parking")
public class GateResource {

    @Inject
    SalesActivityService salesActivityService;
    @Inject
    SalesActivityRepository salesActivityRepository;
    @Inject
    UserService userService;
   
  	@RequestMapping(value = "/enter",
              method = RequestMethod.POST,
              produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
  	public  ResponseEntity<?> enterParkingLot(@PathVariable("apiVersion") final String apiVersion, @RequestBody GateOpenRequest req){
  		User user= userService.getCurrentUser();
  		if(salesActivityService.findInFlightActivityByUser(user).size()!=0){
  		  return new ResponseEntity<>(genericBadReq("User already inside parking lot, can't open gate.", "/gate"),
                  BAD_REQUEST);
  		}
  		if (user.getPlanSubscriptions().size()==0){
  			ParkingPlan subscribedPlan =user.getPlanSubscriptions().iterator().next().getPlanGroup();
  			if (subscribedPlan!=null){
  				if (subscribedPlan.getLotId()==req.getLotId()){
  					SalesActivityDTO salesActivityDTO= salesActivityService.createSaleActivityForPlanUser(user, subscribedPlan);
  					return new ResponseEntity<>(salesActivityDTO,OK);
  					//openGate function
  					
  				}
  				else{
  					return new ResponseEntity<>(genericBadReq("Incorret parking lot.", "/gate"),
  							BAD_REQUEST);
  				}
  			}
  			else{
  				return new ResponseEntity<>(genericBadReq("Error fetching plan data.", "/gate"),
  	                  BAD_REQUEST);
  			}
  		}
  		else{
  			return new ResponseEntity<>(genericBadReq("User haven't subscribed.", "/gate"),
						BAD_REQUEST);
  		}	
  		}
  	 
  	@RequestMapping(value = "/exit",
              method = RequestMethod.POST,
              produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
  	public  ResponseEntity<?> exitParkingLot(@PathVariable("apiVersion") final String apiVersion, @RequestBody GateOpenRequest req){
  		User user= userService.getCurrentUser();
  		if(salesActivityService.findInFlightActivityByUser(user).size()==0){
  		  return new ResponseEntity<>(genericBadReq("User not in the parking lot, can't open gate.", "/gate"),
                  BAD_REQUEST);
  		}
  		ParkingSaleActivity parkingSaleActivity= salesActivityService.findInFlightActivityByUser(user).get(0);
  		parkingSaleActivity.setExitDatetime(DateTime.now());
  		parkingSaleActivity.setParkingStatus("Finished parking");
  		//closeGate Function
  		salesActivityRepository.save(parkingSaleActivity);
  		SalesActivityDTO salesActivityDTO= salesActivityService.contructDTO(parkingSaleActivity, user);
  		return new ResponseEntity<>(salesActivityDTO,OK);
  	}
}
