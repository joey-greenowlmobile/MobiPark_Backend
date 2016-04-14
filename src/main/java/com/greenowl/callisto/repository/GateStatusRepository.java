package com.greenowl.callisto.repository;

import com.greenowl.callisto.domain.GateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.joda.time.DateTime;
import java.util.*;

public interface GateStatusRepository extends JpaRepository<GateStatus,Long>{

	@Query("select g from GateStatus g where g.checkTime > ?1 and g.checkTime < ?2")
	List<GateStatus> getGateStatusBetween(DateTime startTime, DateTime endTime);
	
	
}
