package com.hexaware.Entity;

import java.util.Date;

import jakarta.persistence.*;

@Entity
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int payrollId;

    @ManyToOne
    private Employee employee;

    private Date payDate;
    private double basicSalary;
    private double bonuses = 0;
    private double deductions = 0;
    private double netSalary;
    private double hoursWorked;

    public Payroll() {
    }

    public Payroll(Employee employee, java.sql.Date payDate, double basicSalary, double bonuses, double deductions, double netSalary, double hoursWorked) {
        this.employee = employee;
        this.payDate = payDate;
        this.basicSalary = basicSalary;
        this.bonuses = bonuses;
        this.deductions = deductions;
        this.netSalary = netSalary;
        this.hoursWorked = hoursWorked;
    }

    public int getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(int payrollId) {
        this.payrollId = payrollId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date date) {
        this.payDate = date;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getBonuses() {
        return bonuses;
    }

    public void setBonuses(double bonuses) {
        this.bonuses = bonuses;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    
    @Override
	public String toString() {
		return "Payroll [payrollId=" + payrollId + ", employee=" + employee + ", payDate=" + payDate + ", basicSalary="
				+ basicSalary + ", bonuses=" + bonuses + ", deductions=" + deductions + ", netSalary=" + netSalary
				+ ", hoursWorked=" + hoursWorked + "]";
	}
}