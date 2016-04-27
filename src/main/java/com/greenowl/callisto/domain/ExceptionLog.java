package com.greenowl.callisto.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_EXCEPTION_LOG")
public class ExceptionLog extends AbstractAuditingEntity implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User activityHolder;
	
	@Column(name = "log_message")
    private String logMessage;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getActivityHolder() {
		return activityHolder;
	}

	public void setActivityHolder(User activityHolder) {
		this.activityHolder = activityHolder;
	}
	
	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
    
	
}
