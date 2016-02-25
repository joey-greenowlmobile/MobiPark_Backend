package com.greenowl.callisto.web.rest.dto;

import org.joda.time.DateTime;

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
    
    private DateTime entryDatetime;
    
    private DateTime exitDatetime;
    
    private String parkingStatus;

    private String exceptionFlag;
    public ParkingActivityDTO(){
    
    }
    public ParkingActivityDTO(ParkingActivity activity, User user){
    	this.id=activity.getId();
    	this.lotId=activity.getId();
    	this.userEmail=user.getLogin();
    	this.userPhoneNumber=user.getMobileNumber();
    	this.userPlateNumber=user.getLicensePlate();
    	this.type=activity.getType();
    	this.saleId=activity.getSaleId();
    	this.entryDatetime=activity.getEntryDatetime();
    	this.exitDatetime=activity.getEntryDatetime();
    	this.parkingStatus=activity.getParkingStatus();
    	this.exceptionFlag=activity.getExceptionFlag();
    	
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

	public DateTime getEntryDatetime() {
		return entryDatetime;
	}

	public void setEntryDatetime(DateTime entryDatetime) {
		this.entryDatetime = entryDatetime;
	}

	public DateTime getExitDatetime() {
		return exitDatetime;
	}

	public void setExitDatetime(DateTime exitDatetime) {
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
    
    
    
    
}
