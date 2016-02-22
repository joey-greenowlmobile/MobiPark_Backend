package com.greenowl.callisto.web.rest;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.domain.PlanSubscription;
import com.greenowl.callisto.repository.PlanSubscriptionRepository;
import com.greenowl.callisto.repository.SalesActivityRepository;
import com.greenowl.callisto.service.SalesActivityService;
import com.greenowl.callisto.service.SubscriptionService;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Invoice;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/{apiVersion}/parking")
public class SalesActivityResource {

	private static final Logger LOG = LoggerFactory.getLogger(SalesActivityResource.class);

	@Inject
	private SalesActivityService salesActivityService;

	@Inject
	private SalesActivityRepository salesActivityRepository;

	@Inject
	private SubscriptionService subscriptionService;
	/**
	 * GET /api/{version}/parking/records -> Returns a list of records between a
	 * start and end date of type :type.
	 */
	@RequestMapping(value = "/records", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = false)
	public ResponseEntity<?> getRecords(@PathVariable("apiVersion") final String apiVersion,
			@RequestParam(defaultValue = "all") final String type, @RequestParam(required = false) final Long start,
			@RequestParam(required = false) final Long end) {
		LOG.debug("Checking for records using type = {}, for start date = {} and end date = {}", type, start, end);
		List<ParkingSaleActivity> parkingSaleActivities;
		if (type.equals("all")) {
			parkingSaleActivities = salesActivityRepository.findAll();
		} else {
			DateTime startDate = new DateTime(start*1000);
			DateTime endDate = new DateTime(end*1000);
			parkingSaleActivities = salesActivityService.findAllFilteredActivityBetweenStartAndEndDate(startDate,
					endDate, type);
		}

		List<SalesActivityDTO> salesActivityDTOs = new ArrayList<>();
		salesActivityDTOs.addAll(parkingSaleActivities.stream().map(parkingSaleActivity -> salesActivityService
				.contructDTO(parkingSaleActivity, parkingSaleActivity.getActivityHolder()))
				.collect(Collectors.toList()));
		LOG.info("Returning {} records", salesActivityDTOs.size());
		return new ResponseEntity<>(salesActivityDTOs, OK);

	} 

	
	@RequestMapping(value = "/nextDayTrans", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = false)
	public ResponseEntity<?> generateRecords(@PathVariable("apiVersion") final String apiVersion,
	 @RequestParam(required = false) final Long date) {
		LOG.debug("Generate Pre Transiaction for  day= {}", date);

		DateTime generateDate ;
		if (date==null){
			generateDate = new DateTime();
		}
		else{
			generateDate = new DateTime(date*1000);
		}
		DateTime startDate =generateDate.plusDays(1).withTimeAtStartOfDay();
		DateTime endDate = generateDate.plusDays(2).withTimeAtStartOfDay();
		List<PlanSubscription> nextDaySubscription= subscriptionService.getNextDayRenewSubscription(startDate, endDate);
		List<SalesActivityDTO> preTrans= salesActivityService.createPreTransaction(nextDaySubscription);
		return new ResponseEntity<>(preTrans, OK);
	}
	

	@RequestMapping(value = "/testInvoice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = false)
	public ResponseEntity<?> testInvoice(@PathVariable("apiVersion") final String apiVersion,
			@RequestParam final String cusId, @RequestParam final Long dateTime) {
		Long number= salesActivityService.checkEndOfDayTransaction(new DateTime(dateTime*1000));
		return new ResponseEntity<>(OK);
		
	}
}
