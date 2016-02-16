package com.greenowl.callisto.web.rest;

import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.repository.SalesActivityRepository;
import com.greenowl.callisto.repository.UserRepository;
import com.greenowl.callisto.service.SalesActivityService;
import com.greenowl.callisto.service.util.UserUtil;
import com.greenowl.callisto.web.rest.dto.SalesActivityDTO;
import com.greenowl.callisto.web.rest.dto.UserDTO;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.greenowl.callisto.exception.ErrorResponseFactory.genericBadReq;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/{apiVersion}/parking")
public class SalesActivityResource {
    @Inject
    private SalesActivityService salesActivityService;

    @Inject
    private SalesActivityRepository salesActivityRepository;

    @Inject
    private UserRepository userRepository;

    @RequestMapping(value = "/records",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = false)
    public ResponseEntity<?> getRecords(@PathVariable("apiVersion") final String apiVersion, @RequestParam(defaultValue = "all") final String type, @RequestParam final String startDay,@RequestParam final String endDay) {
        List<ParkingSaleActivity> parkingSaleActivities = new ArrayList<ParkingSaleActivity>();
        if (type.equals("all")) {
            parkingSaleActivities = salesActivityRepository.findAll();
        } else {
            Date startDate = null;
            Date endDate = null;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
            	startDate = df.parse(startDay);
            	System.out.println(startDate.toString());
            	endDate = df.parse(endDay);

            	System.out.println(endDate.toString());
            } catch (ParseException e) {
                return new ResponseEntity<>(genericBadReq("Wrong date format", "/parking"),
                        BAD_REQUEST);
            }
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(startDate);
            calendar1.set(Calendar.HOUR_OF_DAY, 0);
            calendar1.set(Calendar.MINUTE, 0);
            calendar1.set(Calendar.SECOND, 0);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(endDate);

            calendar2.set(Calendar.HOUR_OF_DAY, 0);
            calendar2.set(Calendar.MINUTE, 0);
            calendar2.set(Calendar.SECOND, 0);
            calendar2.add(Calendar.DAY_OF_YEAR, 1);
            System.out.println(calendar2.getTime());
            parkingSaleActivities = salesActivityService.findAllActivityBetween(new DateTime(calendar1.getTime()),new DateTime(calendar2.getTime()));
        }

        List<SalesActivityDTO> salesActivityDTOs = new ArrayList<SalesActivityDTO>();
        List<ParkingSaleActivity> filteredParkingSaleActivities = salesActivityService.filter(parkingSaleActivities, type);
     
        for (ParkingSaleActivity parkingSaleActivity : filteredParkingSaleActivities) {
        	salesActivityDTOs.add(salesActivityService.contructDTO(parkingSaleActivity, parkingSaleActivity.getActivityHolder()));
        }
        return new ResponseEntity<>(salesActivityDTOs, OK);
     
    }

}

