package com.greenowl.callisto.repository;

import com.greenowl.callisto.domain.ParkingSaleActivity;
import com.greenowl.callisto.domain.User;
import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;


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
    
    @Modifying
    @Query("update ParkingSaleActivity u set u.parkingStatus=?1 where u.id = ?2")
    void setParkingStatusById(String parkingStatus, long id);

    @Modifying
    @Query("update ParkingSaleActivity u set u.gateResponse=?1 where u.id = ?2")
    void setGateResponse(String gateResponse, long id);
    
    @Modifying
    @Query("update ParkingSaleActivity u set u.exitDatetime=?1 where u.id = ?2")
    void setExitTime(Timestamp timestamp, long id);
}
