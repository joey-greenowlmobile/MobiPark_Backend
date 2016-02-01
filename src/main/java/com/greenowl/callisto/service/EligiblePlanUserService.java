package com.greenowl.callisto.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.PlanEligibleUser;
import com.greenowl.callisto.repository.ParkingPlanRepository;
import com.greenowl.callisto.repository.PlanEligibleUserRepository;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class EligiblePlanUserService {
 private final Logger LOG = LoggerFactory.getLogger(EligiblePlanUserService.class);
	 
	 @Inject 
	 ParkingPlanRepository parkingPlanRepository;
	 @Inject
	 PlanEligibleUserRepository planEligibleUserRepository;
	 @Inject
	 UserService userService;
	 

	 public PlanEligibleUser getPlansByUserEmailAndPlanId(String userEmail, Long planId){
		 List<PlanEligibleUser> users= planEligibleUserRepository.getEligibleUsersByUserEmail(userEmail);
		 for (PlanEligibleUser user:users ){
			 if (user.getPlanGroup().getId()==planId){
				 return user;
			 }
		 }
		 return null;
	 }
	 public boolean checkSubscribeByPlanIdAndUserEmail(String userEmail, Long planId){
		 List<PlanEligibleUser> users= planEligibleUserRepository.getEligibleUsersByUserEmail(userEmail);
		 for (PlanEligibleUser user:users ){
			 if (user.getPlanGroup().getId()==planId){
				 return user.getSubscribed();
			 }
		 }
		 return false;
	 }
	 
	 public List<PlanEligibleUser> getPlansByUserEmail(String userEmail){
		 return planEligibleUserRepository.getEligibleUsersByUserEmail(userEmail);
		 
	 }
	 public String subscribePlan(String userEmail, Long planId){
		 String userToken= userService.getUser(userEmail).getStripeToken();
		 ParkingPlan parkingPlan=parkingPlanRepository.getOneParkingPlanById(planId);
		 if(checkSubscribeByPlanIdAndUserEmail(userEmail, parkingPlan.getId())==true){
			return "Already Subscribed"; 
		 }
		 else{
			 Stripe.apiKey=Constants.STRIPE_TEST_KEY;
			 Customer cu;
			 try {
				 cu = Customer.retrieve(userToken);
			 } 
			 catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
				| APIException e) {
				 return "Failed at retriving";
			 }
			 Map<String, Object> params = new HashMap<String, Object>();
			 params.put("plan", planId);
			 try {
				 Subscription subscription =cu.createSubscription(params);
				 LOG.debug("Subscribed to the stripe");
				 List<PlanEligibleUser> users= planEligibleUserRepository.getEligibleUsersByUserEmail(userEmail);
				 for (PlanEligibleUser user:users){
					 if (user.getPlanGroup().getId().equals(planId)){
						 user.setSubscribed(true);
						 planEligibleUserRepository.save(user);
						 return subscription.getId();
					 }
				 }
				 LOG.error("Failed at editing eligible user table");
			 }	
			 catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
				| APIException e) {
			// TODO Auto-generated catch block
				 return "Subscribe Failed";
			 }
			 return "Failed unexpected";
		 }
	}
}
