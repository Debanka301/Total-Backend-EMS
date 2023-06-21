package com.backend.EmployeeService.Entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Leaves")
public class Leaves {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int leaveId;
	
	@Column
	private Date startDate;
	
	@Column
	private Date endDate;
	
	@Column
	private String reason;
	
	@ManyToOne
	@JoinColumn(name="id")
	private Employee employee;
	
	public Leaves() {
		
	}

	public Leaves(int leaveId, Date startDate, Date endDate, String reason, Employee employee) {
		super();
		this.leaveId = leaveId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
		this.employee = employee;
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

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	

}
