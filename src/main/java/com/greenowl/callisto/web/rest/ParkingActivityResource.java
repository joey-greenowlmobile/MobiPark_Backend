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

import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.repository.ParkingActivityRepository;
import com.greenowl.callisto.service.ParkingActivityService;
import com.greenowl.callisto.web.rest.dto.ParkingActivityDTO;

@RestController
@RequestMapping("/api/{apiVersion}/parkingActivity")
public class ParkingActivityResource {
	
	@Inject
	private ParkingActivityRepository parkingActivityRepository;
	
	@Inject
	private ParkingActivityService parkingActivityService;
	private static final Logger LOG = LoggerFactory.getLogger(ParkingActivityResource.class);

	@RequestMapping(value = "/records", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = false)
	public ResponseEntity<?> getRecords(@PathVariable("apiVersion") final String apiVersion,
			@RequestParam(defaultValue = "all") final String type, @RequestParam(required = false) final Long start,
			@RequestParam(required = false) final Long end) {
		LOG.debug("Checking for records using type = {}, for start date = {} and end date = {}", type, start, end);
		List<ParkingActivity> parkingActivities;
		if (type.equals("all")) {
			parkingActivities = parkingActivityRepository.findAll();
		} else {
			DateTime startDate = new DateTime(start * 1000);
			DateTime endDate = new DateTime(end * 1000);
			parkingActivities = parkingActivityService.findAllFilteredActivityBetweenStartAndEndDate(startDate, endDate,
					type);
		}

		List<ParkingActivityDTO> parkingActivityDTOs = new ArrayList<>();
		parkingActivityDTOs.addAll(parkingActivities.stream()
				.map(parkingActivity -> parkingActivityService.contructDTO(parkingActivity, parkingActivity.getActivityHolder()))
				.collect(Collectors.toList()));
		LOG.info("Returning {} records", parkingActivityDTOs.size());
		return new ResponseEntity<>(parkingActivityDTOs, OK);

	}
}
