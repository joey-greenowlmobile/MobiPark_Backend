package com.greenowl.callisto.service;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.domain.PlanSubscription;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.ParkingPlanRepository;
import com.greenowl.callisto.repository.SalesActivityRepository;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.stripe.model.Plan;

@Service
public class SalesActivityService {
	@Inject 
	ParkingPlanRepository parkingPlanRepository;
	@Inject
	SalesActivityRepository salesActivityRepository;
    private static final Logger LOG = LoggerFactory.getLogger(SalesActivityService.class);
    
    public SalesActivityDTO saveSaleActivityWithPlan(User user, PlanSubscription plan){
    	return createSaleActivityWithPlan(user,plan);
    	
    }
    
    public SalesActivityDTO createSaleActivityWithPlan(User user, PlanSubscription plan){
    	ParkingSaleActivity newActivity = new ParkingSaleActivity();
    	newActivity.setActivityHolder(user);
    	newActivity.setPlanId(plan.getPlanGroup().getId());
    	newActivity.setPlanName(parkingPlanRepository.getOneParkingPlanById(plan.getPlanGroup().getId()).getPlanName());
    	newActivity.setLotId(plan.getPlanGroup().getLotId());
    	newActivity.setUserEmail(user.getLogin());
    	newActivity.setUserPhoneNumber(user.getMobileNumber());
    	newActivity.setUserLicensePlate(user.getLicensePlate());
    	newActivity.setPlanSubscriptionDate(plan.getPlanStartDate());
    	Double totalCharge=plan.getPlanChargeAmount();
    	newActivity.setChargeAmount(totalCharge);
    	newActivity.setServiceAmount(totalCharge*Constants.SERVICE_FEES_PERCENTAGE);
    	newActivity.setNetAmount(totalCharge*(1-Constants.SERVICE_FEES_PERCENTAGE));
    	newActivity.setPpId(plan.getPaymentProfile().getId());
    	salesActivityRepository.save(newActivity);
    	SalesActivityDTO salesActivityDTO = contructDTO(newActivity, user);
    	return salesActivityDTO;
    	
    	
    }
    
    public List<ParkingSaleActivity> findAllActivityBetween (DateTime startTime, DateTime endTime){
    	return salesActivityRepository.getParkingSaleActivityBetween(startTime, endTime);
    }
    
    public SalesActivityDTO contructDTO(ParkingSaleActivity activity, User user){
    	SalesActivityDTO salesActivityDTO = new SalesActivityDTO(activity.getId(), activity.getLotId(), user, activity.getPlanId(), 
    			activity.getPlanSubscriptionDate(), activity.getPlanExpiryDate(), activity.getChargeAmount(),activity.getServiceAmount(),
    			activity.getNetAmount(), activity.getPpId(), activity.getEntryDatetime(), activity.getEntryDatetime(),
    			activity.getParkingStatus(), activity.getExceptionFlag(), activity.getInvoiceId());
    	return salesActivityDTO;
    }
}
