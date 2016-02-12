package com.greenowl.callisto.service;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.ParkingPlan;
import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.domain.PlanSubscription;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.ParkingPlanRepository;
import com.greenowl.callisto.repository.SalesActivityRepository;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Invoice;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

@Service
public class SalesActivityService {

    @Inject
    private ParkingPlanRepository parkingPlanRepository;
    @Inject
    private SalesActivityRepository salesActivityRepository;

    private static final Logger LOG = LoggerFactory.getLogger(SalesActivityService.class);

    public SalesActivityDTO saveSaleActivityWithPlan(User user, PlanSubscription plan) {
        return createSaleActivityWithPlan(user, plan);

    }

    public SalesActivityDTO createSaleActivityWithPlan(User user, PlanSubscription plan) {
        Stripe.apiKey = Constants.STRIPE_TEST_KEY;
        ParkingSaleActivity newActivity = new ParkingSaleActivity();
        Map<String, Object> invoiceParams = new HashMap<String, Object>();
        invoiceParams.put("limit", 3);
        invoiceParams.put("customer", user.getStripeToken());
        try {
            List<Invoice> invoices = Invoice.all(invoiceParams).getData();
            for (Invoice invoice : invoices) {
                if (invoice.getSubscription().equals(plan.getStripeId())) {
                    newActivity.setInvoiceId(invoice.getId());
                    break;
                }
            }
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
                | APIException e) {

        }
        newActivity.setActivityHolder(user);
        newActivity.setPlanId(plan.getPlanGroup().getId());
        newActivity.setPlanName(parkingPlanRepository.getOneParkingPlanById(plan.getPlanGroup().getId()).getPlanName());
        newActivity.setLotId(plan.getPlanGroup().getLotId());
        newActivity.setUserEmail(user.getLogin());
        newActivity.setUserPhoneNumber(user.getMobileNumber());
        newActivity.setUserLicensePlate(user.getLicensePlate());
        newActivity.setPlanSubscriptionDate(new java.sql.Timestamp(plan.getPlanStartDate().getMillis()));
        Double totalCharge = plan.getPlanChargeAmount();
        newActivity.setChargeAmount(totalCharge);
        newActivity.setServiceAmount(totalCharge * Constants.SERVICE_FEES_PERCENTAGE);
        newActivity.setNetAmount(totalCharge * (1 - Constants.SERVICE_FEES_PERCENTAGE));
        newActivity.setPpId(plan.getPaymentProfile().getId());
        salesActivityRepository.save(newActivity);
        SalesActivityDTO salesActivityDTO = contructDTO(newActivity, user);
        return salesActivityDTO;


    }

    public SalesActivityDTO createSaleActivityForPlanUser(User user, ParkingPlan plan) {
        ParkingSaleActivity newActivity = new ParkingSaleActivity();
        newActivity.setActivityHolder(user);
        newActivity.setPlanId(plan.getId());
        newActivity.setPlanName(parkingPlanRepository.getOneParkingPlanById(plan.getId()).getPlanName());
        newActivity.setLotId(plan.getLotId());
        newActivity.setUserEmail(user.getLogin());
        newActivity.setUserPhoneNumber(user.getMobileNumber());
        newActivity.setUserLicensePlate(user.getLicensePlate());
        Double totalCharge = 0.0;
        newActivity.setChargeAmount(totalCharge);
        newActivity.setServiceAmount(totalCharge * Constants.SERVICE_FEES_PERCENTAGE);
        newActivity.setNetAmount(totalCharge * (1 - Constants.SERVICE_FEES_PERCENTAGE));
        newActivity.setEntryDatetime(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
        newActivity.setParkingStatus("Parked");
        salesActivityRepository.save(newActivity);
        SalesActivityDTO salesActivityDTO = contructDTO(newActivity, user);
        return salesActivityDTO;
    }

    public List<ParkingSaleActivity> findAllActivityBetween(Timestamp startTime, Timestamp endTime) {
        return salesActivityRepository.getParkingSaleActivityBetween(startTime, endTime);
    }

    public SalesActivityDTO contructDTO(ParkingSaleActivity activity, User user) {
        SalesActivityDTO salesActivityDTO = new SalesActivityDTO(activity.getId(), activity.getLotId(), user, activity.getPlanId(),
                activity.getPlanSubscriptionDate(), activity.getPlanExpiryDate(), activity.getChargeAmount(), activity.getServiceAmount(),
                activity.getNetAmount(), activity.getPpId(), activity.getEntryDatetime(), activity.getEntryDatetime(),
                activity.getParkingStatus(), activity.getExceptionFlag(), activity.getInvoiceId());
        salesActivityDTO.setGateResponse(activity.getGateResponse());
        return salesActivityDTO;
    }

    public List<ParkingSaleActivity> findInFlightActivityByUser(User user) {
        List<ParkingSaleActivity> parkingSaleActivities = salesActivityRepository.getParkingSaleActivitiesByUser(user);
        List<ParkingSaleActivity> inFlightActivities = new ArrayList<ParkingSaleActivity>();
        for (ParkingSaleActivity activity : parkingSaleActivities) {
            if (activity.getEntryDatetime() != null && activity.getExitDatetime() == null) {
                inFlightActivities.add(activity);
            }
        }
        return inFlightActivities;
    }

    public List<ParkingSaleActivity> filter(List<ParkingSaleActivity> parkingSaleActivities, Boolean sale,
                                            Boolean record, Boolean inFlight) {
        List<ParkingSaleActivity> filteredList = new ArrayList<ParkingSaleActivity>();
        for (ParkingSaleActivity activity : parkingSaleActivities) {
            if (inFlight == true) {
                if (activity.getEntryDatetime() != null & activity.getExitDatetime() == null) {
                    filteredList.add(activity);
                }
                break;
            }
            if (sale == true) {

                if (record == true) {
                    if (activity.getEntryDatetime() != null && activity.getChargeAmount() != null) {
                        filteredList.add(activity);
                    } else {
                        break;
                    }
                } else {
                    if (activity.getChargeAmount() > 0) {
                        filteredList.add(activity);
                    } else {
                        break;
                    }
                }
            } else {
                if (record == true) {
                    if (activity.getEntryDatetime() != null) {
                        filteredList.add(activity);
                    } else {
                        break;
                    }
                } else {
                    filteredList.add(activity);
                }
            }
        }
        return filteredList;
    }
    
    
    public ParkingSaleActivity getParkingSaleActivityById(long id){
    	return salesActivityRepository.getParkingSaleActivityById(id);        	
    }
    
    public void updateParkingStatus(String parkingStatus, long id){
    	salesActivityRepository.setParkingStatusById(parkingStatus, id);
    }
    
    public void updateGateResponse(String gateResponse, long id){
    	salesActivityRepository.setGateResponse(gateResponse, id);
    }
    
    public void updateExitTime(java.sql.Timestamp timestamp, long id){
    	salesActivityRepository.setExitTime(timestamp, id);
    }
}
