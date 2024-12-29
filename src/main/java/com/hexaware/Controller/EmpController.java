package com.hexaware.Controller;

import com.hexaware.Entity.Employee;
import com.hexaware.Entity.Leaves;

import com.hexaware.Entity.Users;
import com.hexaware.Entity.Payroll;
import com.hexaware.Exceptions.EmployeeCustomExceptions.EmployeeNotFoundException;
import com.hexaware.Exceptions.EmployeeCustomExceptions.LeaveRequestFailedException;
import com.hexaware.Service.EmpService;
import com.hexaware.Service.UserService;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class EmpController {

    @Autowired
    EmpService ser;
    @Autowired
    UserService userService;
    
    //fetch employee details
    @GetMapping("/emp/profile")
    public ResponseEntity<Employee> viewProfile(Principal principal) {
    	String username=principal.getName();        
    	if (principal.getName() == null) {
            throw new EmployeeNotFoundException("User is not authenticated");
        }
        Users user=userService.getUserByUsername(username);
        int userid=user.getUserId();
        Employee emp=ser.getEmployeeByUserId(userid);
        
        if (emp != null) {
            return ResponseEntity.ok(emp);
        } else {
            throw new EmployeeNotFoundException("Employee not found with username: " + username);
        }
    }
    
    //update employee details
    @PutMapping("/emp/update_info/{id}")
    public ResponseEntity<Employee> updatePersonalInfo(@PathVariable int id, @RequestBody Employee updatedInfo) {
        Employee updatedEmp = ser.updatePersonalInfo(id, updatedInfo);
        if (updatedEmp != null) {
            return new ResponseEntity<>(updatedEmp, HttpStatus.OK);
        } else {
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
        }
    }
    
    //fetch employee leaves    
    @GetMapping("/emp/leaves/{empId}")
    public List<Leaves> getLeavesByEmployee(@PathVariable int empId) {
        return ser.getLeavesByEmployee(empId);
    }

    //request leave
    @PostMapping("/emp/leave_request/{id}")
    public ResponseEntity<Leaves> requestLeave(@PathVariable int id, @RequestBody Leaves leaveRequest) {
        Leaves leaveReq = ser.requestLeave(id, leaveRequest);
        if (leaveReq != null) {
            return new ResponseEntity<>(leaveReq, HttpStatus.CREATED);
        } else {
            throw new LeaveRequestFailedException("Failed to submit leave request for Employee with ID " + id);
        }
    }
    
    //fetch employee payroll    
    @GetMapping("/emp/emp_payroll/{empId}")
    public List<Payroll> getPayrollbyEmployeeId(@PathVariable int empId) {
        return (List<Payroll>) ser.getPayrollByEmpId(empId);
    }
    
}
