package com.greenowl.callisto.repository;

import org.joda.time.DateTime;
import java.util.List;

import com.greenowl.callisto.domain.ParkingValTicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

public interface ParkingValTicketStatusRepository extends JpaRepository<ParkingValTicketStatus, String>{
    
	@Query("select p from ParkingValTicketStatus p where p.id = ?1 ")
	public ParkingValTicketStatus getParkingValTicketStatusById(long id);
	
	@Query("select p from ParkingValTicketStatus p where p.ticketNo = ?1 and p.ticketType = ?2")
	public List<ParkingValTicketStatus> getParkingValTicketStatusByTicketnoAndTickettype(long ticketNo, int ticketType);
	
	@Modifying
	@Query("update ParkingValTicketStatus p set p.validateDateTime = ?1 where p.id = ?2")
	public void updateValidateTime(DateTime time, long id);
	
	@Modifying
	@Query("update ParkingValTicketStatus p set p.validatedFlag = ?1 where p.id = ?2")
	public void updateValidatedFlag(int flag, long id);
	
}
