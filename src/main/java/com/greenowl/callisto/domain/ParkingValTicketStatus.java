package com.greenowl.callisto.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@Table(name = "T_PARKING_VAL_TICKET_STATUS")
public class ParkingValTicketStatus implements Serializable {
    @Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "created_date_time")
	private Timestamp createdDateTime;
	
	@Column(name = "validate_date_time")
	private Timestamp validateDateTime;
	
	@Column(name = "validated_flag")
	private Boolean validatedFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public Timestamp getValidateDateTime() {
		return validateDateTime;
	}

	public void setValidateDateTime(Timestamp validateDateTime) {
		this.validateDateTime = validateDateTime;
	}

	public Boolean getValidatedFlag() {
		return validatedFlag;
	}

	public void setValidatedFlag(Boolean validatedFlag) {
		this.validatedFlag = validatedFlag;
	}
	
	
	
}
