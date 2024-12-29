package com.hexaware.Entity;

import jakarta.persistence.*;
import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private int empId;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", length = 10, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "hire_date", nullable = false)
    private Date hireDate;

    @Column(name = "salary", nullable = false)
    private double salary;
    
    private int leavesLeft;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dept_id", referencedColumnName = "dept_id") 
    private Department department;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;


    // Constructors
    public Employee() {
    }


	public Employee(int empId, String firstName, String lastName, String email, String phoneNumber, Date hireDate,
			double salary, int leavesLeft, Department department, Users user) {
		super();
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.hireDate = hireDate;
		this.salary = salary;
		this.leavesLeft = leavesLeft;
		this.department = department;
		this.user = user;
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


	public int getLeavesLeft() {
		return leavesLeft;
	}


	public void setLeavesLeft(int leavesLeft) {
		this.leavesLeft = leavesLeft;
	}


	public Department getDepartment() {
		return department;
	}


	public void setDepartment(Department department) {
		this.department = department;
	}


	public Users getUser() {
		return user;
	}


	public void setUser(Users user) {
		this.user = user;
	}


	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", hireDate=" + hireDate + ", salary=" + salary + ", leavesLeft="
				+ leavesLeft + ", department=" + department + ", user=" + user + "]";
	}
    
    

}