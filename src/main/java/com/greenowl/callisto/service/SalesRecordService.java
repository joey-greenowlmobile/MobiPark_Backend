package com.greenowl.callisto.service;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.PlanSubscription;
import com.greenowl.callisto.domain.SalesRecord;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.ParkingPlanRepository;
import com.greenowl.callisto.repository.PlanSubscriptionRepository;
import com.greenowl.callisto.repository.SalesRecordRepository;
import com.greenowl.callisto.repository.UserRepository;
import com.greenowl.callisto.util.SalesActivityUtil;
import com.greenowl.callisto.web.rest.dto.SalesRecordDTO;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Invoice;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalesRecordService {

    @Inject
    private SalesRecordRepository salesRecordRepository;

    private static final Logger LOG = LoggerFactory.getLogger(SalesRecordService.class);

    public SalesRecordDTO savePlanSaleRecord(User user, PlanSubscription plan) throws AuthenticationException,
            InvalidRequestException, APIConnectionException, CardException, APIException {
        return createPlanSaleRecord(user, plan);

    }

    private List<SalesRecord> findAllSalesRecordsBetween(DateTime startTime, DateTime endTime) {
        return salesRecordRepository.getSalesRecordsBetween(startTime, endTime);
    }

    /**
     * Return all the activities between start date and end date based on the
     * filtered type.
     *
     * @param startTime
     * @param endTime
     * @param type
     * @return
     */
    public List<SalesRecord> findAllFilteredSalesRecordsBetweenStartAndEndDate(DateTime start, DateTime end,
                                                                               String type) {
        List<SalesRecord> filteredList = new ArrayList<>();
        List<SalesRecord> salesRecords = findAllSalesRecordsBetween(start, end);
        for (SalesRecord salesRecord : salesRecords) {
            switch (type.toLowerCase()) {
                case "all":
                    filteredList.add(salesRecord);
                    break;
                case "subscription":
                    if (salesRecord.getType().equals("Subscription")) {
                        filteredList.add(salesRecord);
                    }
                    break;
                case "daily":
                    if (salesRecord.getType().equals("Daily")) {
                        filteredList.add(salesRecord);
                    }
                    break;
                default:
                    break;
            }
        }
        return filteredList;
    }

    /**
     * Create sales record when user subscribe to the plan.
     *
     * @param user
     * @param plan
     * @return
     */
    public SalesRecordDTO createPlanSaleRecord(User user, PlanSubscription plan) throws AuthenticationException,
            InvalidRequestException, APIConnectionException, CardException, APIException {
        Stripe.apiKey = Constants.STRIPE_TEST_KEY;
        SalesRecord newRecord = new SalesRecord();
        Map<String, Object> invoiceParams = new HashMap<String, Object>();
        invoiceParams.put("limit", 3);
        invoiceParams.put("customer", user.getStripeToken());

        List<Invoice> invoices = Invoice.list(invoiceParams).getData();
        for (Invoice invoice : invoices) {
            if (invoice.getSubscription().equals(plan.getStripeId())) {
                newRecord.setInvoiceId(invoice.getId());
                break;
            }
        }
        if (newRecord.getInvoiceId().isEmpty()) {
            return null;
        }
        newRecord.setActivityHolder(user);
        newRecord.setPlanId(plan.getPlanGroup().getId());
        newRecord.setLotId(plan.getPlanGroup().getLotId());
        Double totalCharge = plan.getPlanChargeAmount();
        newRecord.setChargeAmount(totalCharge);
        newRecord.setServiceAmount(totalCharge * Constants.SERVICE_FEES_PERCENTAGE);
        newRecord.setNetAmount(totalCharge * (1 - Constants.SERVICE_FEES_PERCENTAGE));
        newRecord.setType("Subscription");
        newRecord.setPpId(plan.getPaymentProfile().getId());
        salesRecordRepository.save(newRecord);
        return SalesActivityUtil.constructDTO(newRecord, user);

    }

}
