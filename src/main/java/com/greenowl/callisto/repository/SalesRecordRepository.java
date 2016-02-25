package com.greenowl.callisto.repository;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.greenowl.callisto.domain.SalesRecord;
import com.greenowl.callisto.domain.User;

public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {
	@Query("select u from SalesRecord u where u.id = ?1")
	SalesRecord getSalesRecordById(Long id);

	@Query("select u from SalesRecord u where u.createdDate > ?1 and u.createdDate < ?2")
	List<SalesRecord> getSalesRecordsBetween(DateTime startTime, DateTime endTime);

	@Query("select u from SalesRecord u where u.createdDate > ?1 and u.createdDate < ?2 and u.activityHolder = ?3")
	List<SalesRecord> getSalesRecordsBetweenForUser(DateTime startTime, DateTime endTime,
			User activityHolder);

	@Query("select u from SalesRecord u where u.activityHolder = ?1")
	List<SalesRecord> getSalesRecordsByUser(User activityHolder);
	
	@Query("select u from SalesRecord u where u.invoiceId = ?1")
	SalesRecord getSalesRecordsByInvoiceId(String invoiceId);
	
	@Query("select u from SalesRecord u where u.lotId = ?1")
	List<SalesRecord> getSalesRecordsByLotId(Long lotId);
	
	@Query("select u from SalesRecord u where u.type = ?1")
	List<SalesRecord> getSalesRecordsByType(String type);
}
