package com.backend.TaxService.Entity;

import jakarta.validation.constraints.NotNull;

public class TaxInput {
	
	@NotNull(message="Employee Id cannot be null")
	private int empId;
	
	@NotNull(message="Salary cannot be null")
	private int salary;
	
	public TaxInput(int empId, int salary) {
		super();
		this.empId = empId;
		this.salary = salary;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	
	
	

}
