package com.hexaware.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hexaware.Entity.Audit;
import com.hexaware.Entity.Employee;
import com.hexaware.Entity.Payroll;
import com.hexaware.Entity.Users;
import com.hexaware.Exceptions.EmployeeCustomExceptions.EmployeeNotFoundException;
import com.hexaware.Exceptions.PayrollCustomExceptions.PayrollNotFoundException;
import com.hexaware.Repository.AuditRepository;
import com.hexaware.Repository.EmployeeRepo;
import com.hexaware.Repository.PayrollRepo;

@Service
public class PayrollService {

    @Autowired PayrollRepo payrollRepo;
    @Autowired EmployeeRepo employeeRepo;
    @Autowired AuditRepository auditRepo;
    @Autowired UserService userService;

    private static final double HR_DEPARTMENT_RATE = 50.0;
    private static final double IT_DEPARTMENT_RATE = 70.0;
    private static final double SALES_DEPARTMENT_RATE = 60.0;
    private static final double DEVELOPER_DEPARTMENT_RATE = 80.0;
    private static final double FINANCE_DEPARTMENT_RATE = 65.0;

    //remove payroll
    public void removePayroll(int id) {
        Payroll p = payrollRepo.findById(id).orElse(null);
        if(p!=null) {
            payrollRepo.delete(p);
            logAudit("Deleted payroll for Employee ID: " + p.getEmployee().getEmpId());
        }
        else {
        	throw new PayrollNotFoundException("Payroll not found");
        }
    }

    //update payroll
    public Payroll updatePayroll(int pId, Payroll p) {
        Payroll existingPayroll = payrollRepo.findById(pId).orElse(null);
        if(p!=null) {
        	Employee e=existingPayroll.getEmployee();
        	if(e!=null) {
        		int id=e.getEmpId();
                Employee employee = employeeRepo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
                double netSalary=0.0;
                existingPayroll.setEmployee(employee);
                existingPayroll.setPayDate(existingPayroll.getPayDate());
                existingPayroll.setBasicSalary(p.getBasicSalary());
                existingPayroll.setBonuses(p.getBonuses());
                existingPayroll.setDeductions(p.getDeductions());
                netSalary = calculateNetSalary(p.getBasicSalary(), p.getBonuses(), p.getDeductions());
                existingPayroll.setNetSalary(netSalary);
                existingPayroll.setHoursWorked(p.getHoursWorked());
                Payroll updatedPayroll = payrollRepo.save(existingPayroll);
                logAudit("Updated payroll for Employee ID: " + updatedPayroll.getEmployee().getEmpId());
                return updatedPayroll;
        	}
        	else {
        		throw new EmployeeNotFoundException("Employee ot Found.");
        	}
        }
        else {
        	throw new PayrollNotFoundException("Payroll not found");
        }
        
    }

    //calculate payroll
    public Payroll calculatePayroll(Payroll p, int empId) {
        Employee e = employeeRepo.findById(empId).orElseThrow(() -> new RuntimeException("Employee not found"));
        double hourlyRate=0.0, basicSalary=0.0, bonuses=0.0, deductions=0.0, netSalary=0.0;
        if(e!=null) {
        	hourlyRate = getHourlyRate(e);
            basicSalary = p.getHoursWorked() * hourlyRate;
            bonuses = calculateBonuses(basicSalary);
            deductions = calculateDeductions(basicSalary);
            netSalary = calculateNetSalary(basicSalary, bonuses, deductions);
        }
        Payroll payroll = new Payroll();
        payroll.setEmployee(e);
        payroll.setPayDate(p.getPayDate());
        payroll.setBasicSalary(basicSalary);
        payroll.setBonuses(bonuses);
        payroll.setDeductions(deductions);
        payroll.setNetSalary(netSalary);
        payroll.setHoursWorked(p.getHoursWorked());

        Payroll savedPayroll = payrollRepo.save(payroll);
        logAudit("Calculated payroll for Employee ID: " + savedPayroll.getEmployee().getEmpId());
        return savedPayroll;
    }

    //find payroll by payroll ID
    public Payroll findByPayrollId(int payrollId) {
        return payrollRepo.findById(payrollId)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));
    }

    //find payroll by employee ID
    public List<Payroll> findPayrollsByEmployeeId(int employeeId) {
        return payrollRepo.findByEmployee_EmpId(employeeId);
    }

    //calculate net salary
    private double calculateNetSalary(double basicSalary, double bonuses, double deductions) {
        return basicSalary + bonuses - deductions;
    }

    //get hourly rate for the employee
    private double getHourlyRate(Employee employee) {
        String deptName = employee.getDepartment().getDeptName();
        switch (deptName) {
            case "HR":
                return HR_DEPARTMENT_RATE;
            case "IT":
                return IT_DEPARTMENT_RATE;
            case "Sales":
                return SALES_DEPARTMENT_RATE;
            case "Developer":
                return DEVELOPER_DEPARTMENT_RATE;
            case "Finance":
                return FINANCE_DEPARTMENT_RATE;
            default:
                return 40.0;
        }
    }

    //calculate bonus
    private double calculateBonuses(double basicSalary) {
        return basicSalary * 0.10;
    }

    //calculate deductions
    private double calculateDeductions(double basicSalary) {
        return basicSalary * 0.05;
    }

    //get payroll audit logs
    public List<Audit> getPayrollAuditLogs() {
        return auditRepo.findByActionContaining("payroll");
    }
    
    //log audit
    private void logAudit(String actionDescription) {
        Users currentUser = userService.getCurrentLoggedInUser();
        Audit auditLog = new Audit(actionDescription, currentUser, new java.sql.Timestamp(System.currentTimeMillis()));
        auditRepo.save(auditLog);
    }

}
