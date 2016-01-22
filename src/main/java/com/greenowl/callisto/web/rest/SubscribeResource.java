package com.greenowl.callisto.web.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.PlanEligibleUser;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.service.EligiblePlanUserService;
import com.greenowl.callisto.service.ParkingPlanService;
import com.greenowl.callisto.service.UserService;
import com.greenowl.callisto.web.rest.dto.ParkingPlanDTO;
import com.greenowl.callisto.web.rest.dto.ResponseDTO;
import com.greenowl.callisto.web.rest.dto.user.CreateUserRequest;
import static org.springframework.http.HttpStatus.OK;
/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/{apiVersion}/plan")
public class SubscribeResource {

    private final Logger LOG = LoggerFactory.getLogger(SubscribeResource.class);
    @Inject 
    ParkingPlanService parkingPlanService;
    @Inject
    EligiblePlanUserService eligiblePlanUserService;
    @Inject
    UserService userService;
   
	@RequestMapping(value = "/allPlan",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public  ResponseEntity<?> getAllPlan(@PathVariable("apiVersion") final String apiVersion,@RequestParam final String userEmail ) {
        List<PlanEligibleUser> users = eligiblePlanUserService.getPlansByUserEmail(userEmail);
        if (users==null){
        	return new ResponseEntity<>(OK);
        }
        else{
        	List<ParkingPlanDTO> parkingPlanDTOs = new ArrayList<ParkingPlanDTO>();
        for(PlanEligibleUser user: users ) {
        	ParkingPlan plan=user.getPlanGroup();
        	if (plan!=null){
        		parkingPlanDTOs.add(parkingPlanService.createParkingPlanInformation(plan));
        	}
        		
        	}
        return new ResponseEntity<>(parkingPlanDTOs,OK);
        }	
	}
	
	@RequestMapping(value = "/subscribePlan",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
	 public  ResponseEntity<?> subscribePlan(@PathVariable("apiVersion") final String apiVersion,@RequestParam final String userEmail,@RequestParam final String planName ) {
		String response =eligiblePlanUserService.subscribePlan(userEmail, planName);
		ResponseDTO responseDTO=new ResponseDTO(response);
		return new ResponseEntity<>(responseDTO,OK);
	}
}
