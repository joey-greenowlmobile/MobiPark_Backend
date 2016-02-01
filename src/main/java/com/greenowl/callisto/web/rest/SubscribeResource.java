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
import com.greenowl.callisto.domain.PaymentProfile;
import com.greenowl.callisto.domain.PlanEligibleUser;
import com.greenowl.callisto.domain.PlanSubscription;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.PaymentProfileRepository;
import com.greenowl.callisto.repository.PlanSubscriptionRepository;
import com.greenowl.callisto.service.EligiblePlanUserService;
import com.greenowl.callisto.service.ParkingPlanService;
import com.greenowl.callisto.service.SalesActivityService;
import com.greenowl.callisto.service.SubscriptionService;
import com.greenowl.callisto.service.UserService;
import com.greenowl.callisto.web.rest.dto.ParkingPlanDTO;
import com.greenowl.callisto.web.rest.dto.ResponseDTO;
import com.greenowl.callisto.web.rest.dto.user.CreateUserRequest;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/{apiVersion}/user")
public class SubscribeResource {

    private final Logger LOG = LoggerFactory.getLogger(SubscribeResource.class);
    
    private static final String PLAN_NOT_FOUND="Unable to find suitable plan.";
    
    @Inject 
    ParkingPlanService parkingPlanService;
    @Inject
    EligiblePlanUserService eligiblePlanUserService;
    @Inject
    UserService userService;
    @Inject
    SubscriptionService subscriptionService;
    @Inject
    PaymentProfileRepository paymentProfileRepository;
    @Inject 
    SalesActivityService salesActivityService;
    @Inject
    PlanSubscriptionRepository planSubscriptionRepository;
    
	@RequestMapping(value = "/plan",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public  ResponseEntity<?> getAllPlan(@PathVariable("apiVersion") final String apiVersion) {
		User currentUser =userService.getCurrentUser();
        List<PlanEligibleUser> users = eligiblePlanUserService.getPlansByUserEmail(currentUser.getLogin());
        if (users.size()==0){
        	return new ResponseEntity<>(genericBadReq(PLAN_NOT_FOUND, "/plan"),
                    BAD_REQUEST);
        }
        else{
        	if (users.size()==1){
        		ParkingPlanDTO parkingPlanDTO=parkingPlanService.createParkingPlanInformation(users.get(0).getPlanGroup());
        		return new ResponseEntity<>(parkingPlanDTO,OK);
        	}
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
	
	@RequestMapping(value = "/plan",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
	 public  ResponseEntity<?> subscribePlan(@PathVariable("apiVersion") final String apiVersion,@RequestParam final Long planId,@RequestParam final String login) {
		User user= userService.getUser(login);
		String response=null;
		PaymentProfile paymentProfile= paymentProfileRepository.getPaymentProfilesByUser(user).get(0);
		if(paymentProfile!=null){
			response =eligiblePlanUserService.subscribePlan(user.getLogin(), planId);}
		else{
			response="Failed to find payment profile.";
		}
			if (response.startsWith("sub_")){
				PlanSubscription planSubscription= subscriptionService.createPlanSubscription(user, planId, paymentProfile.getId(),response);
				if (planSubscription!=null){
					salesActivityService.saveSaleActivityWithPlan(user, planSubscription);
				return new ResponseEntity<>(OK);
				}
				else{
					return new ResponseEntity<>(genericBadReq("Failed at adding to the table", "/register"),
							BAD_REQUEST);
				}
			}
			return new ResponseEntity<>(genericBadReq(response, "/register"),
			BAD_REQUEST);
	}
}
