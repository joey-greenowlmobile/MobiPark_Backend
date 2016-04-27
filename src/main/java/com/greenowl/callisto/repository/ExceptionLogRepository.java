package com.greenowl.callisto.repository;

import org.joda.time.DateTime;

import com.greenowl.callisto.domain.ExceptionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface ExceptionLogRepository extends JpaRepository<ExceptionLog, Long>{

	@Query("select el from ExceptionLog el where el.id = ?1")
	ExceptionLog getExceptionLogById(Long id);
	
	@Query("select el from ExceptionLog el where el.createdDate > ?1 and el.createdDate < ?2")
	List<ExceptionLog>  getExceptionLogBetween(DateTime startTime, DateTime endTime);
	
	
}
