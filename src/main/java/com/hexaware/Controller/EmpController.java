package com.hexaware.Controller;

import com.hexaware.Entity.Employee;
import com.hexaware.Entity.Leaves;

import com.hexaware.Entity.Users;
import com.hexaware.Entity.Payroll;
import com.hexaware.Exceptions.EmployeeCustomExceptions.EmployeeNotFoundException;
import com.hexaware.Exceptions.EmployeeCustomExceptions.LeaveRequestFailedException;
import com.hexaware.Exceptions.EmployeeCustomExceptions.ProfileUpdateFailedException;
import com.hexaware.Service.EmpService;
import com.hexaware.Service.UserService;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/emp")
public class EmpController {

    @Autowired EmpService empService;
    @Autowired UserService userService;
    
    //fetch employee details
    @GetMapping("/profile")
    public ResponseEntity<Employee> viewProfile(Principal principal) {
    	String username=principal.getName();  
    	System.out.println(username);
    	if (principal.getName() == null) {
            throw new EmployeeNotFoundException("User is not authenticated");
        }
        Users user=userService.getUserByUsername(username);
        int userid=user.getUserId();
        System.out.println(userid);
        Employee emp=empService.getEmployeeByUserId(userid);
        if (emp != null) {
            return ResponseEntity.ok(emp);
        } else {
            throw new EmployeeNotFoundException("Employee not found with username: " + username);
        }
    }
    
    //update employee details
    @PutMapping("/update_info/{id}")
    public ResponseEntity<Employee> updatePersonalInfo(@PathVariable int id, @RequestBody Employee updatedInfo) {
        Employee e = empService.updatePersonalInfo(id, updatedInfo);
        if (e != null) {
            return new ResponseEntity<>(e, HttpStatus.OK);
        } else {
            throw new ProfileUpdateFailedException("Employee with ID " + id + " not found.");
        }
    }
    
    //fetch employee leaves    
    @GetMapping("/leaves/{empId}")
    public List<Leaves> getLeavesByEmployee(@PathVariable int empId) {
        return empService.getLeavesByEmployee(empId);
    }

    //request leave
    @PostMapping("/leave_request/{id}")
    public ResponseEntity<Leaves> requestLeave(@PathVariable int id, @RequestBody Leaves request) {
        Leaves l = empService.requestLeave(id, request);
        if (l != null) {
            return new ResponseEntity<>(l, HttpStatus.CREATED);
        } else {
            throw new LeaveRequestFailedException("Failed to submit leave request for Employee with ID " + id);
        }
    }
    
    //fetch employee payroll    
    @GetMapping("/emp_payroll/{empId}")
    public List<Payroll> getPayrollbyEmployeeId(@PathVariable int empId) {
        return (List<Payroll>) empService.getPayrollByEmpId(empId);
    }
    
}
