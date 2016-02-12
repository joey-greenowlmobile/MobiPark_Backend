package com.greenowl.callisto.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.greenowl.callisto.domain.ParkingValTicketStatus;
import com.greenowl.callisto.repository.ParkingValTicketStatusRepository;
import com.greenowl.callisto.repository.SalesActivityRepository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.*;
import javax.inject.Inject;


@Service
public class ParkingValTicketStatusService {

	@Inject
	private ParkingValTicketStatusRepository pvtsRepository;
	
		
	public void createParkingValTicketStatus(ParkingValTicketStatus parkingValTicketStatus){
		pvtsRepository.save(parkingValTicketStatus);		
	}
	
	public void updateValidateTime(Timestamp time, long id){
		pvtsRepository.updateValidateTime(time, id);
	}
	
	public void updateValidatedFlag(boolean flag){
		pvtsRepository.updateValidatedFlag(flag);
	}
		
	
}
