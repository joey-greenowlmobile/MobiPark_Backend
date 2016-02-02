package com.greenowl.callisto.repository;

import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.domain.User;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalesActivityRepository extends JpaRepository<ParkingSaleActivity, String> {

    @Query("select u from ParkingSaleActivity u where u.id = ?1")
    ParkingSaleActivity getParkingSaleActivityById(Long id);

    @Query("select u from ParkingSaleActivity u where u.createdDate > ?1 and u.createdDate <?2")
    List<ParkingSaleActivity> getParkingSaleActivityBetween(DateTime startTime, DateTime endTime);

    @Query("select u from ParkingSaleActivity u where u.activityHolder = ?1")
    List<ParkingSaleActivity> getParkingSaleActivitiesByUser(User activityHolder);

    @Query("select u from ParkingSaleActivity u where u.invoiceId = ?1")
    List<ParkingSaleActivity> getParkingSaleActivitiesByInvoiceId(String invoiceId);

}
