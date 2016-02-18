package com.greenowl.callisto.domain;

import java.io.Serializable;
import org.joda.time.DateTime;
import javax.persistence.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "T_PARKING_VAL_TICKET_STATUS")
public class ParkingValTicketStatus implements Serializable {
    @Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "created_date_time")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime createdDateTime;
	
	@Column(name = "validate_date_time")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime validateDateTime;
	
	@Column(name = "validated_flag")
	private int validatedFlag;

	@Column(name = "ticket_no")
	private Long ticketNo;
	
	@Column(name = "ticket_type")
	private int ticketType;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(DateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public DateTime getValidateDateTime() {
		return validateDateTime;
	}

	public void setValidateDateTime(DateTime validateDateTime) {
		this.validateDateTime = validateDateTime;
	}

	public int getValidatedFlag() {
		return validatedFlag;
	}

	public void setValidatedFlag(int validatedFlag) {
		this.validatedFlag = validatedFlag;
	}

	public Long getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(Long ticketNo) {
		this.ticketNo = ticketNo;
	}

	public int getTicketType() {
		return ticketType;
	}

	public void setTicketType(int ticketType) {
		this.ticketType = ticketType;
	}
	
	
	
}
