package com.hexaware.Entity;

import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "leaves")
public class Leaves {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private int leaveId;

    @ManyToOne
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    @Column(name = "leave_type", length = 50, nullable = false)
    private String leaveType;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "days", nullable = false)
    private int days;

    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private LeaveStatus status;

    // Enum for leave status
    public enum LeaveStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    // Constructors
    public Leaves() {
    }

    public Leaves(int leaveId, Employee employee, String leaveType, Date startDate, Date endDate, int days,
                  LeaveStatus status) {
        this.leaveId = leaveId;
        this.employee = employee;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
        this.status = status;
    }

    // Getters and Setters
    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        this.employee = employee;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
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

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public void approve() {
        this.status = LeaveStatus.APPROVED;
    }

    public void reject() {
        this.status = LeaveStatus.REJECTED;
    }

    @Override
    public String toString() {
        return "Leaves [leaveId=" + leaveId + ", employee=" + employee + ", leaveType=" + leaveType + ", startDate="
                + startDate + ", endDate=" + endDate + ", days=" + days + ", status=" + status + "]";
    }
}
