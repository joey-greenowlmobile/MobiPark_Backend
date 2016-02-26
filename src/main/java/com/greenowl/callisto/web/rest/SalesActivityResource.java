package com.greenowl.callisto.web.rest;

import static org.springframework.http.HttpStatus.OK;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.domain.PlanSubscription;
import com.greenowl.callisto.domain.SalesRecord;
import com.greenowl.callisto.repository.SalesActivityRepository;
import com.greenowl.callisto.repository.SalesRecordRepository;
import com.greenowl.callisto.service.SalesActivityService;
import com.greenowl.callisto.service.SalesRecordService;
import com.greenowl.callisto.service.SubscriptionService;
import com.greenowl.callisto.util.ApiUtil;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.greenowl.callisto.web.rest.dto.SalesRecordDTO;

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

	@Inject
	private SalesRecordService salesRecordService;

	@Inject
	private SalesRecordRepository salesRecordRepository;

	/**
	 * GET /api/{version}/parking/records -> Returns a list of records between a
	 * start and end date of type :type.
	 */

	@RequestMapping(value = "/records", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = false)
	public ResponseEntity<?> getRecords(@PathVariable("apiVersion") final String apiVersion,
			@RequestParam(defaultValue = "all") final String type, @RequestParam(required = false) final Long start,
			@RequestParam(required = false) final Long end) {
		if (ApiUtil.getVersion().getValue() == 2) {
			LOG.info("Returning V2 for Get Records API.");
			return getRecordsV2(type, start, end);
		}
		LOG.info("Returning V1 for Get Records API.");
		return getRecordsV1(type, start, end);
	}
	/**
	 * Get parking and sales records for old table.
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	private ResponseEntity<?> getRecordsV1(String type, Long start, Long end) {
		LOG.debug("Checking for records using type = {}, for start date = {} and end date = {}", type, start, end);
		List<ParkingSaleActivity> parkingSaleActivities;
		if (type.equals("all")) {
			parkingSaleActivities = salesActivityRepository.findAll();
		} else {
			DateTime startDate = new DateTime(start * 1000);
			DateTime endDate = new DateTime(end * 1000);
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
	
	/**
	 * Get sales records in new table.
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	private ResponseEntity<?> getRecordsV2(String type, Long start, Long end) {
		LOG.debug("Checking for records using type = {}, for start date = {} and end date = {}", type, start, end);
		List<SalesRecord> salesRecords;
		if (type.equals("all")) {
			salesRecords = salesRecordRepository.findAll();
		} else {
			DateTime startDate = new DateTime(start * 1000);
			DateTime endDate = new DateTime(end * 1000);
			salesRecords = salesRecordService.findAllFilteredSalesRecordsBetweenStartAndEndDate(startDate, endDate,
					type);
		}

		List<SalesRecordDTO> salesRecordDTOs = new ArrayList<>();
		salesRecordDTOs.addAll(salesRecords.stream()
				.map(salesRecord -> salesRecordService.contructDTO(salesRecord, salesRecord.getActivityHolder()))
				.collect(Collectors.toList()));
		LOG.info("Returning {} records", salesRecordDTOs.size());
		return new ResponseEntity<>(salesRecordDTOs, OK);

	}

	@RequestMapping(value = "/nextDayTrans", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = false)
	public ResponseEntity<?> generateRecords(@PathVariable("apiVersion") final String apiVersion,
			@RequestParam(required = false) final Long date) {
		LOG.debug("Generate Pre Transiaction for  day= {}", date);

		DateTime generateDate;
		if (date == null) {
			generateDate = new DateTime();
		} else {
			generateDate = new DateTime(date * 1000);
		}
		DateTime startDate = generateDate.plusDays(1).withTimeAtStartOfDay();
		DateTime endDate = generateDate.plusDays(2).withTimeAtStartOfDay();
		List<PlanSubscription> nextDaySubscription = subscriptionService.getNextDayRenewSubscription(startDate,
				endDate);
		List<SalesActivityDTO> preTrans = salesActivityService.createPreTransaction(nextDaySubscription);
		return new ResponseEntity<>(preTrans, OK);
	}

	@RequestMapping(value = "/testInvoice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = false)
	public ResponseEntity<?> testInvoice(@PathVariable("apiVersion") final String apiVersion,
			@RequestParam final String cusId, @RequestParam final Long dateTime) {
		Long number = salesActivityService.checkEndOfDayTransaction(new DateTime(dateTime * 1000));
		return new ResponseEntity<>(OK);

	}
}
