package com.backend.TaxService.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name="Tax")
public class Tax {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int taxId;
	
	@Column
	@NotNull(message="Employee Id cannot be null")
	private int empId;
	
	@Column
	@NotNull(message="Salary cannot be null")
	private int salary;
	
	@Column
	@NotNull(message="Tax Percentage cannot be null")
	private int taxPerc;
	
	@Column
	@NotNull(message="Tax Amount cannot be null")
	private int taxAmount;
	
	@Column
	@NotNull(message="Inhand Salary cannot be null")
	private int inHand;
	
	public Tax() {
		
	}
	
	public Tax(int taxId, int empId, int salary, int taxPerc, int taxAmount, int inHand) {
		super();
		this.taxId = taxId;
		this.empId = empId;
		this.salary = salary;
		this.taxPerc = taxPerc;
		this.taxAmount = taxAmount;
		this.inHand = inHand;
	}

	public int getTaxId() {
		return taxId;
	}

	public void setTaxId(int taxId) {
		this.taxId = taxId;
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

	public int getTaxPerc() {
		return taxPerc;
	}

	public void setTaxPerc(int taxPerc) {
		this.taxPerc = taxPerc;
	}

	public int getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(int taxAmount) {
		this.taxAmount = taxAmount;
	}

	public int getInHand() {
		return inHand;
	}

	public void setInHand(int inHand) {
		this.inHand = inHand;
	}
	
	
	
	
	

}