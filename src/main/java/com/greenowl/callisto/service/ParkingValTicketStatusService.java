package com.greenowl.callisto.service;

import org.joda.time.DateTime;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;

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
	
	
	public void createParkingValTicketStatus(long ticketNo, int ticketType, DateTime time){
		ParkingValTicketStatus p = new ParkingValTicketStatus();
		p.setTicketNo(ticketNo);
		p.setTicketType(ticketType);
		p.setCreatedDateTime(time);
		pvtsRepository.save(p);
	}
		
	public void createParkingValTicketStatus(ParkingValTicketStatus parkingValTicketStatus){
		pvtsRepository.save(parkingValTicketStatus);		
	}
	
	public void updateValidateTime(DateTime time, long id){
		pvtsRepository.updateValidateTime(time, id);
	}
	
	public void updateValidatedFlag(int flag, long id){
		pvtsRepository.updateValidatedFlag(flag,id);
	}
	
	public ParkingValTicketStatus getParkingValTicketStatusById(long id){
		return pvtsRepository.getParkingValTicketStatusById(id);
	}
	
	public List<ParkingValTicketStatus> getParkingValTicketStatusByTicketnoAndTicketType(long ticketNo, int ticketType){
		return pvtsRepository.getParkingValTicketStatusByTicketnoAndTickettype(ticketNo, ticketType);
	}
	
}
