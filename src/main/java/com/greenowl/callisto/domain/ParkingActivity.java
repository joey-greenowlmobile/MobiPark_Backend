package com.greenowl.callisto.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_PARKING_ACTIVITY")
public class ParkingActivity extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "lot_id", nullable = false)
    private Long lotId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User activityHolder;

    @Column(name = "type")
    private String type;

    @Column(name = "sale_id")
    private Long saleId;

    @Column(name = "entry_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime entryDatetime;

    @Column(name = "exit_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime exitDatetime;

    @Column(name = "parking_status")
    private String parkingStatus;

    @Column(name = "exception_flag")
    private String exceptionFlag;

    @Column(name = "gate_response")
    private String gateResponse;

    @Column(name = "device_info")
    private String deviceInfo;
    
    @Column(name = "open_log")
    private String openLog;

    public ParkingActivity() {
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

    public User getActivityHolder() {
        return activityHolder;
    }

    public void setActivityHolder(User activityHolder) {
        this.activityHolder = activityHolder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public String getGateResponse() {
        return gateResponse;
    }

    public void setGateResponse(String gateResponse) {
        this.gateResponse = gateResponse;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

	public String getOpenLog() {
		return openLog;
	}

	public void setOpenLog(String openLog) {
		this.openLog = openLog;
	}
    
    
}
