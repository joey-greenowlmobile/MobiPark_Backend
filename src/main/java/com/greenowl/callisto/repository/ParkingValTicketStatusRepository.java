package com.greenowl.callisto.repository;

import java.sql.Timestamp;

import com.greenowl.callisto.domain.ParkingValTicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

public interface ParkingValTicketStatusRepository extends JpaRepository<ParkingValTicketStatus, String>{
    
	@Modifying
	@Query("update ParkingValTicketStatus t set t.validateDateTime = ?1 where t.id = ?2")
	public void updateValidateTime(Timestamp time, long id);
	
	@Modifying
	@Query("update ParkingValTicketStatus t set t.validatedFlag = ?1 where t.id = ?2")
	public void updateValidatedFlag(boolean flag);
	
}
