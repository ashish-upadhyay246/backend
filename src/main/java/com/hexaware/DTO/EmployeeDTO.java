package com.hexaware.DTO;

import java.util.Date;

public class EmployeeDTO {
    
    private int empId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date hireDate;
    private double salary;
    private int departmentId;  
    private int userId;  
    private int leavesLeft;

    // Constructors
    public EmployeeDTO() {
    }

	public EmployeeDTO(int empId, String firstName, String lastName, String email, String phoneNumber, Date hireDate,
			double salary, int departmentId, int userId, int leavesLeft) {
		super();
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.hireDate = hireDate;
		this.salary = salary;
		this.departmentId = departmentId;
		this.userId = userId;
		this.leavesLeft = leavesLeft;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getLeavesLeft() {
		return leavesLeft;
	}

	public void setLeavesLeft(int leavesLeft) {
		this.leavesLeft = leavesLeft;
	}

}
