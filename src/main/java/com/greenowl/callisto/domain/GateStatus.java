package com.greenowl.callisto.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "T_GATE_STATUS")
public class GateStatus implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Access(AccessType.PROPERTY)
	private Long id;
	
	@Column(name = "gate_id")
	private String gateId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "check_time")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime checkTime;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGateId() {
		return gateId;
	}
	public void setGateId(String gateId) {
		this.gateId = gateId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public DateTime getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(DateTime checkTime) {
		this.checkTime = checkTime;
	}	
	
}
