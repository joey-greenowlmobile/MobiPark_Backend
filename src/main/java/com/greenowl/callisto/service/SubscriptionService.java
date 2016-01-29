package com.greenowl.callisto.service;

import java.sql.Timestamp;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jadira.usertype.dateandtime.joda.PersistentDateTime;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.PaymentProfile;
import com.greenowl.callisto.domain.PlanSubscription;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.ParkingPlanRepository;
import com.greenowl.callisto.repository.PaymentProfileRepository;
import com.greenowl.callisto.repository.PlanSubscriptionRepository;
import com.greenowl.callisto.repository.UserRepository;

@Service
public class SubscriptionService {

    private final Logger LOG = LoggerFactory.getLogger(SubscriptionService.class);
    @Inject
    PlanSubscriptionRepository planSubriptionRepository;

    @Inject
    ParkingPlanRepository parkingPlanRepository;
    @Inject
    PaymentProfileRepository paymentProfileRepository;
    
    
    public PlanSubscription getPlanSubscriptionById(Long id){
    	return planSubriptionRepository.getPlanSubscriptionById(id);
    }
    
    public PlanSubscription createPlanSubscription(User user, Long planId, Long paymentProfileId){

    	PlanSubscription planSubscription = new PlanSubscription();
    	DateTime createdDate = DateTime.now();
    		ParkingPlan parkingPlan = parkingPlanRepository.getOneParkingPlanById(planId);
    
    		PaymentProfile paymentProfile = paymentProfileRepository.getPaymentProfileById(paymentProfileId);
    		if (parkingPlan!=null & paymentProfile!=null){
    		planSubscription.setUser(user);
    		planSubscription.setPlanGroup(parkingPlan);
    		planSubscription.setPaymentProfile(paymentProfile);
    		planSubscription.setPlanStartDate(createdDate);
    		planSubscription.setPlanChargeAmount( parkingPlan.getUnitChargeAmount());
    		planSubriptionRepository.save(planSubscription);
    		return planSubscription;}
    		else{
    			return null;
    		}
    	
    	
    	
    }
}
