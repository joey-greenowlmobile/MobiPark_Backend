package com.greenowl.callisto.web.rest;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.repository.SalesActivityRepository;
import com.greenowl.callisto.repository.UserRepository;
import com.greenowl.callisto.service.SalesActivityService;
import com.greenowl.callisto.service.UserService;
import com.greenowl.callisto.service.util.UserUtil;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.greenowl.callisto.web.rest.dto.UserDTO;

@RestController
@RequestMapping("/api/{apiVersion}/parking")
public class SalesActivityResource {
    @Inject
    SalesActivityService salesActivityService;
    @Inject
    SalesActivityRepository salesActivityRepository;
    @Inject
    UserRepository userRepository;
	@RequestMapping(value = "/records",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
	public  ResponseEntity<?> getRecords(@PathVariable("apiVersion") final String apiVersion, @RequestParam(defaultValue = "all") final String type, @RequestParam final String day, 
			@RequestParam final Boolean sale, @RequestParam final Boolean record, @RequestParam final Boolean inFlight) {
		List<ParkingSaleActivity> parkingSaleActivities= new ArrayList<ParkingSaleActivity>();
		if(type.equals("all")){
			parkingSaleActivities= salesActivityRepository.findAll();
		}
		else{	
			Date date=null;
			DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
			try {
				date = df.parse(day);
			} 
			catch (ParseException e) {
				return new ResponseEntity<>(genericBadReq("Wrong date format", "/parking"),
						BAD_REQUEST);	
			}
			DateTime dateTime = new DateTime(date);
			DateTime startOfTheDay = dateTime.withTimeAtStartOfDay();
			DateTime endOfTheDay = dateTime.plusDays(1).withTimeAtStartOfDay();
			parkingSaleActivities=  salesActivityService.findAllActivityBetween(startOfTheDay, endOfTheDay);
		}
			
		List<SalesActivityDTO> salesActivityDTOs= new ArrayList<SalesActivityDTO>();
		List<ParkingSaleActivity> filteredParkingSaleActivities =salesActivityService.filter(parkingSaleActivities,sale,record, inFlight);
		if(inFlight==false){
			for (ParkingSaleActivity parkingSaleActivity: filteredParkingSaleActivities){
				salesActivityDTOs.add(salesActivityService.contructDTO(parkingSaleActivity,parkingSaleActivity.getActivityHolder()));
			}
			return new ResponseEntity<>(salesActivityDTOs,OK);
		}
		else{
			List<UserDTO> userDTOs= new ArrayList<UserDTO>();
			for (ParkingSaleActivity parkingSaleActivity: filteredParkingSaleActivities){
				UserDTO userDTO = UserUtil.getUserDTO(userRepository.findSingleUserByLogin(parkingSaleActivity.getUserEmail()));
				userDTOs.add(userDTO);
			}
			
				return new ResponseEntity<>(userDTOs,OK);
			}
		}
		
	}

