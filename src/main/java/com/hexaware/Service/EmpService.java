package com.hexaware.Service;

import com.hexaware.Entity.Employee;


import com.hexaware.Entity.Leaves;
import com.hexaware.Entity.Leaves.LeaveStatus;
import com.hexaware.Entity.Payroll;
import com.hexaware.Repository.EmployeeRepo;
import com.hexaware.Repository.LeaveRepo;
import com.hexaware.Repository.PayrollRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpService {

    @Autowired
    private EmployeeRepo employeeRepository;

    @Autowired
    private LeaveRepo leaveRepository;
    
    @Autowired
    private PayrollRepo payrollRepo;
    

    //fetch employee by employee id
	public Employee getEmployeeByUserId(int id) {
		return employeeRepository.findByEmpId(id);
	}

    //update employee details    
    public Employee updatePersonalInfo(int id, Employee info) {
        Optional<Employee> emp = employeeRepository.findById(id);
        if (emp.isPresent()) {
            Employee existingEmployee = emp.get();
            existingEmployee.setFirstName(info.getFirstName());
            existingEmployee.setLastName(info.getLastName());
            existingEmployee.setEmail(info.getEmail());
            existingEmployee.setPhoneNumber(info.getPhoneNumber());
            Employee updatedEmployee = employeeRepository.save(existingEmployee);
            return updatedEmployee;
        } else {
            return null;
        }
    }

    //request for leave
    public Leaves requestLeave(int employeeId, Leaves leaveRequest) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            long daysBetween = java.time.Duration.between(leaveRequest.getStartDate().toLocalDate().atStartOfDay(),
                                                         leaveRequest.getEndDate().toLocalDate().atStartOfDay()).toDays();
            leaveRequest.setDays((int) daysBetween + 1);
            leaveRequest.setEmployee(employee);
            if (leaveRequest.getDays() > employee.getLeavesLeft()) {
                throw new IllegalArgumentException("The available leaves for the year are not enough. Please contact your manager.");
            }
            if (leaveRequest.getStatus() == null) {
                leaveRequest.setStatus(LeaveStatus.PENDING);
            }
            return leaveRepository.save(leaveRequest);
        } else {
            return null;
        }
    }

    //get leaves by employee ID
    public List<Leaves> getLeavesByEmployee(int empId) {
        return leaveRepository.findByEmployee_EmpId(empId);
    }
    
    //get payroll by employee ID
	public List<Payroll> getPayrollByEmpId(int empId) {
		return payrollRepo.findByEmployee_EmpId(empId);
	}

}
