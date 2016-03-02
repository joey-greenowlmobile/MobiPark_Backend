package com.greenowl.callisto.repository;

import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.domain.User;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkingActivityRepository extends JpaRepository<ParkingActivity, Long> {


    @Query("select u from ParkingActivity u where u.id = ?1")
    ParkingActivity getParkingActivityById(Long id);

    @Query("select u from ParkingActivity u where u.createdDate > ?1 and u.createdDate < ?2")
    List<ParkingActivity> getParkingActivityBetween(DateTime startTime, DateTime endTime);

    @Query("select u from ParkingActivity u where u.createdDate > ?1 and u.createdDate < ?2 and u.activityHolder = ?3")
    List<ParkingActivity> getParkingActivityBetweenForUser(DateTime startTime, DateTime endTime,
                                                           User activityHolder);

    @Query("select u from ParkingActivity u where u.activityHolder = ?1")
    List<ParkingActivity> getParkingActivitiesByUser(User activityHolder);

    @Query("select u from ParkingActivity u where u.activityHolder = ?1 and u.type = ?2")
    ParkingActivity getParkingActivityByUserAndType(User user, String type);

    @Query("select u from ParkingActivity u where u.type = ?1")
    List<ParkingActivity> getParkingActivitiesByType(String type);


    @Query("select u from ParkingActivity u where u.lotId = ?1")
    List<ParkingActivity> getParkingActivitiesByLotId(Long lotId);

    Page<ParkingActivity> findByActivityHolder(User u, Pageable page);

    @Modifying
    @Query("update ParkingActivity u set u.parkingStatus=?1 where u.id = ?2")
    void setParkingStatusById(String parkingStatus, long id);

    @Modifying
    @Query("update ParkingActivity u set u.gateResponse=?1 where u.id = ?2")
    void setGateResponse(String gateResponse, long id);

    @Modifying
    @Query("update ParkingActivity u set u.exitDatetime=?1 where u.id = ?2")
    void setExitTime(DateTime time, long id);

}
