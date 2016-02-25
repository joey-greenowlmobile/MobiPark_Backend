package com.greenowl.callisto.domain;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "T_PARKING_ACTIVITY")
public class ParkingActivity extends AbstractAuditingEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Access(AccessType.PROPERTY)
	private Long id;

	@Column(name = "lot_id", nullable = false)
	private Long lotId;

	@ManyToOne(fetch = FetchType.LAZY)
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

}
