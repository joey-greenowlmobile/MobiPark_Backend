package com.greenowl.callisto.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.PlanEligibleUser;
import com.greenowl.callisto.repository.ParkingPlanRepository;
import com.greenowl.callisto.repository.PlanEligibleUserRepository;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class EligiblePlanUserService {
 private final Logger LOG = LoggerFactory.getLogger(EligiblePlanUserService.class);
	 
	 @Inject 
	 ParkingPlanRepository parkingPlanRepository;
	 @Inject
	 PlanEligibleUserRepository planEligibleUserRepository;
	 

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
}
