package com.backend.LeavesService.Entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="Leaves")
public class Leaves {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int leaveId;
	
	@Column
	@NotNull(message="Start date cannot be null")
	private Date startDate;
	
	@Column
	@NotNull(message="End date cannot be null")
	private Date endDate;
	
	@Column
	@NotNull(message="Reason cannot be null")
	private String reason;
	
	@Column
	@NotNull(message="Employee Id cannot be null")
	private int id;
	

	public Leaves() {
		
	}

	public Leaves(int leaveId, Date startDate, Date endDate, String reason) {
		super();
		this.leaveId = leaveId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
	}

	public int getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(int leaveId) {
		this.leaveId = leaveId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
