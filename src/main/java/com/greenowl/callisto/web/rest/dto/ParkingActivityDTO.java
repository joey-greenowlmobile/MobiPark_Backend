package com.greenowl.callisto.web.rest.dto;

import com.greenowl.callisto.domain.ParkingActivity;
import com.greenowl.callisto.domain.User;

public class ParkingActivityDTO {

    private Long id;

    private Long lotId;

    private String userEmail;

    private String userPhoneNumber;

    private String userPlateNumber;

    private String type;

    private Long saleId;

    private Long entryDatetime;

    private Long exitDatetime;

    private String parkingStatus;

    private String exceptionFlag;

    private Long createdDate;

    public ParkingActivityDTO() {

    }

    public ParkingActivityDTO(ParkingActivity activity, User user) {
        this.id = activity.getId();
        this.lotId = activity.getId();
        this.userEmail = user.getLogin();
        this.userPhoneNumber = user.getMobileNumber();
        this.userPlateNumber = user.getLicensePlate();
        this.type = activity.getType();
        this.saleId = activity.getSaleId();
        if (activity.getEntryDatetime() != null) {
            this.entryDatetime = activity.getEntryDatetime().getMillis();
        }
        if (activity.getEntryDatetime() != null) {
            this.exitDatetime = activity.getEntryDatetime().getMillis();
        }
        this.parkingStatus = activity.getParkingStatus();
        this.exceptionFlag = activity.getExceptionFlag();
        if (activity.getCreatedDate() != null) {
            this.createdDate = activity.getCreatedDate().getMillis();
        }
    }

    public ParkingActivityDTO(Long id, Long lotId, String userEmail, String userPhoneNumber, String userPlateNumber, String type,
                              Long saleId, Long entryDatetime, Long exitDatetime, String parkingStatus, String exceptionFlag, Long createdDate) {
        this.id = id;
        this.lotId = lotId;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.userPlateNumber = userPlateNumber;
        this.type = type;
        this.saleId = saleId;
        this.entryDatetime = entryDatetime;
        this.exitDatetime = exitDatetime;
        this.parkingStatus = parkingStatus;
        this.exceptionFlag = exceptionFlag;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserPlateNumber() {
        return userPlateNumber;
    }

    public void setUserPlateNumber(String userPlateNumber) {
        this.userPlateNumber = userPlateNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Long getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(Long entryDatetime) {
        this.entryDatetime = entryDatetime;
    }

    public Long getExitDatetime() {
        return exitDatetime;
    }

    public void setExitDatetime(Long exitDatetime) {
        this.exitDatetime = exitDatetime;
    }

    public String getParkingStatus() {
        return parkingStatus;
    }

    public void setParkingStatus(String parkingStatus) {
        this.parkingStatus = parkingStatus;
    }

    public String getExceptionFlag() {
        return exceptionFlag;
    }

    public void setExceptionFlag(String exceptionFlag) {
        this.exceptionFlag = exceptionFlag;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "ParkingActivityDTO{" +
                "createdDate=" + createdDate +
                ", id=" + id +
                ", lotId=" + lotId +
                ", userEmail='" + userEmail + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", userPlateNumber='" + userPlateNumber + '\'' +
                ", type='" + type + '\'' +
                ", saleId=" + saleId +
                ", entryDatetime=" + entryDatetime +
                ", exitDatetime=" + exitDatetime +
                ", parkingStatus='" + parkingStatus + '\'' +
                ", exceptionFlag='" + exceptionFlag + '\'' +
                '}';
    }
}
