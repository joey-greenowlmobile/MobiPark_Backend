package com.greenowl.callisto.service;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.PlanSubscription;
import com.greenowl.callisto.domain.SalesRecord;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.PlanSubscriptionRepository;
import com.greenowl.callisto.repository.SalesRecordRepository;
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
	
	@Inject
	private PlanSubscriptionRepository planSubscriptionRepository;

	private static final Logger LOG = LoggerFactory.getLogger(SalesRecordService.class);

	public SalesRecordDTO savePlanSaleRecord(User user, PlanSubscription plan) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException {
		return createPlanSaleRecord(user, plan);

	}

	private List<SalesRecord> findAllSalesRecordsBetween(DateTime startTime, DateTime endTime) {
		return salesRecordRepository.getSalesRecordsBetween(startTime, endTime);
	}

	/**
	 * Return all the records between start date and end date based on the
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

	public boolean validNewTransaction(User user, DateTime startDate, DateTime endDate) {
		List<SalesRecord> records = salesRecordRepository.getSalesRecordsBetweenForUser(startDate, endDate, user);
		for (SalesRecord record : records) {
			if (record.getChargeAmount() != 0 && record.getType().equals("PRE_TRANSACTION")) {
				return false;
			}
		}
		return true;
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

	public List<SalesRecordDTO> createPreTransaction(List<PlanSubscription> nextDaySubscriptions) {
		List<SalesRecordDTO> records = new ArrayList<>();
		for (PlanSubscription plan : nextDaySubscriptions) {
			records.add(createrecord(plan));
		}
		return records;
	}

	private SalesRecordDTO createrecord(PlanSubscription subscription) {
		SalesRecord record = new SalesRecord();
		record.setPlanId(subscription.getPlanGroup().getId());
		record.setLotId(subscription.getPlanGroup().getLotId());
		record.setActivityHolder(subscription.getUser());
		Double totalCharge = subscription.getPlanGroup().getUnitChargeAmount();
		record.setChargeAmount(totalCharge);
		record.setServiceAmount(totalCharge * Constants.SERVICE_FEES_PERCENTAGE);
		record.setNetAmount(totalCharge * (1 - Constants.SERVICE_FEES_PERCENTAGE));
		record.setType("PRE_TRANSACTION");
		salesRecordRepository.save(record);
		return SalesActivityUtil.constructDTO(record, subscription.getUser());
	}

//  /**
//   * Get all the pre-transactions and check the status on stripe, update if
//   * the payment has been made. (Not fully tested yet)
//   *
//   * @param startDate
//   * @return
//   */
  public Long checkEndOfDayTransaction(DateTime startDate) {
      Long number = (long) 0;
      List<SalesRecord> records =salesRecordRepository
              .getSalesRecordsByType("PRE_TRANSACTION");
      System.out.println(records.size());
      Stripe.apiKey = Constants.STRIPE_TEST_KEY;
      for (SalesRecord record : records) {
          LOG.debug("record ={}", record.getId());
          User user = record.getActivityHolder();
          Long time = record.getCreatedDate().getMillis() / 1000;
          Map<String, Object> invoiceParams = new HashMap<String, Object>();
          invoiceParams.put("limit", 3);
          invoiceParams.put("customer", user.getStripeToken());
          try {
              List<Invoice> invoices = Invoice.list(invoiceParams).getData();
              if (planSubscriptionRepository.getPlanSubscriptionByUser(user).size() == 0) {
                  LOG.debug("User {} is not subscribed", user.getLogin());
                  break;
              }
              PlanSubscription subscription = planSubscriptionRepository.getPlanSubscriptionByUser(user).get(0);
              String subToken = subscription.getStripeId();
              for (Invoice invoice : invoices) {
                  if (invoice.getSubscription().equals(subToken) && invoice.getDate() > time) {
                      if (invoice.getPaid()) {
                          if (invoice.getAmountDue().doubleValue() / 100 == subscription.getPlanChargeAmount()) {
                              LOG.debug("The amount match for subscription ={}", subToken);
                              record.setInvoiceId(invoice.getId());
                              record.setType("PAYMENT_COMPLETED");
                              salesRecordRepository.save(record);
                              number += 1;
                              break;
                          } else {
                              LOG.debug("The amount doesn't match for subscription ={}", subToken);
                          }
                      } else {
                          LOG.debug("Unpaid invoice {} for user {}", invoice.getId(), user.getStripeToken());
                      }
                  }
              }
          } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
                  | APIException e) {
              LOG.debug("Failed at talking to stripe for user ={}", user.getLogin());
              e.printStackTrace();
          }
      }
      return number;
  }

}
