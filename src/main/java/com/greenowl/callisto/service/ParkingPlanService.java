package com.greenowl.callisto.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.PaymentProfile;
import com.greenowl.callisto.domain.PlanEligibleUser;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.factory.PaymentProfileFactory;
import com.greenowl.callisto.repository.ParkingPlanRepository;
import com.greenowl.callisto.repository.PlanEligibleUserRepository;
import com.greenowl.callisto.service.util.PaymentProfileUtil;
import com.greenowl.callisto.web.rest.dto.ParkingPlanDTO;
import com.greenowl.callisto.web.rest.dto.PaymentProfileDTO;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class ParkingPlanService {
	 private final Logger LOG = LoggerFactory.getLogger(ParkingPlanService.class);
	 
	 @Inject 
	 ParkingPlanRepository parkingPlanRepository;
	 @Inject
	 PlanEligibleUserRepository planEligibleUserRepository;
	 
	 public Set<PlanEligibleUser> getEligibleUsersByPlanId(Long id){
		 ParkingPlan parkingPlan=getParkingPlanById(id);
		if (parkingPlan!=null){
			Set<PlanEligibleUser> planEligibleUsers=parkingPlan.getPlanEligibleUsers();
			return planEligibleUsers;
		}
	 
	 return null;
	 }
	
	 public ParkingPlan getParkingPlanById(Long id){
		 return parkingPlanRepository.getOneParkingPlanById(id);
	 }
	 
	 public List<ParkingPlan> getAllRucurringPlan() {
		 return parkingPlanRepository.getRecurringParkingPlans();
	 }
	 
	 public ParkingPlan etParkingPlanByName(String planName){
		 return parkingPlanRepository.getOneParkingPlanByPlanName(planName);
	 }
	 
	 public ParkingPlanDTO createParkingPlanInformation(ParkingPlan parkingPlan){
		 	ParkingPlanDTO parkingPlanDTO = new ParkingPlanDTO(parkingPlan.getId(), 
		 														parkingPlan.getPlanName(),
		 														parkingPlan.getPlanDesc(), 
		 														parkingPlan.getUnitChargeAmount(), 
		 														parkingPlan.getMonthlyPlan(), 
		 														parkingPlan.getPlanTerminatedDays());
	    		return parkingPlanDTO;
	    	}
	 
}
