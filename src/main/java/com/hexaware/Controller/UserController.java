package com.hexaware.Controller;

import com.hexaware.Entity.Employee;
import com.hexaware.Entity.Leaves;
import com.hexaware.Entity.Users;
import com.hexaware.Exceptions.UserCustomExceptions.DuplicateUsernameException;
import com.hexaware.Exceptions.UserCustomExceptions.UserNotFoundException;
import com.hexaware.Service.UserService;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.relation.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired UserService userService;

    //register user
    @PostMapping("/register")
    public ResponseEntity<Users> admin_addUser(@RequestBody Users u) {
        Users userObj = userService.admin_addUser(u); 
        return new ResponseEntity<>(userObj, HttpStatus.CREATED);
    }

    //login user
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Users u) {
        String token = userService.verify(u);
        String role = userService.getRole(u.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("jwt", token);
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    //edit user name
    @PostMapping("/admin_editUsername/{username}/{newname}")
    public ResponseEntity<String> admin_editUserName(@PathVariable String username, @PathVariable String newname) {
        String result = userService.admin_editUserName(username, newname);
        if (result.equals("Username already exists")) {
            throw new DuplicateUsernameException("Username '" + newname + "' already exists");
        }
        if (result.equals("User not found")) {
            throw new UserNotFoundException("User with username '" + username + "' not found");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //edit user role
    @PostMapping("/admin_editUserRole/{username}/{role}")
    public ResponseEntity<String> admin_editUserRole(@PathVariable String username, @PathVariable String role) throws RoleNotFoundException {
        try {
            Users.Role newRole = Users.Role.valueOf(role.toUpperCase());
            String result = userService.admin_editUserRole(username, newRole);
            if (result.equals("User not found")) {
                throw new UserNotFoundException("User with username '" + username + "' not found");
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundException("Role '" + role + "' is invalid");
        }
    }

    //edit user password
    @PostMapping("/admin_editUserPwd/{username}/{pwd}")
    public ResponseEntity<String> admin_editUserPwd(@PathVariable String username, @PathVariable String pwd) {
        String result = userService.admin_editUserPwd(username, pwd);
        if (result.equals("User not found")) {
            throw new UserNotFoundException("User with username '" + username + "' not found");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    //add an employee
    @PostMapping("/admin_addEmployeeDetails/{uId}/{dId}")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee e, @PathVariable int dId, @PathVariable int uId) {
        Employee emp = userService.createEmployee(e, dId, uId);
        return new ResponseEntity<>(emp, HttpStatus.CREATED);
    }

    //get all users
    @GetMapping("/admin_getUserData")
    public ResponseEntity<List<Users>> getData() {
    	List<Users> userObj = userService.admin_showAll();
    	if(userObj.isEmpty()) {
    		return new ResponseEntity<List<Users>>(userObj,HttpStatus.NO_CONTENT);
    	}
    	return new ResponseEntity<List<Users>>(userObj,HttpStatus.OK);
    }
    
    //get all employees
    @GetMapping("/admin_getAllEmployees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> empObj = userService.getAllEmployees();
    	if(empObj.isEmpty()) {
    		return new ResponseEntity<List<Employee>>(empObj,HttpStatus.NO_CONTENT);
    	}
    	return new ResponseEntity<List<Employee>>(empObj,HttpStatus.OK);
    }

    //get specific user
    @GetMapping("/admin_getUserData/{userID}")
    public ResponseEntity<?> getById(@PathVariable int userID) {
        Users userObj = userService.admin_getDataById(userID);
        System.out.println(userObj);
        if (userObj != null) {
        	return new ResponseEntity<>(userObj, HttpStatus.OK);
        }
        else {
        	throw new UserNotFoundException("User with ID '" + userID + "' not found");
        }
    }
    
    //get specific employee's leaves
    @GetMapping("/admin_getEmployeeLeaves/{employeeId}")
    public ResponseEntity<List<Leaves>> getEmployeeLeaves(@PathVariable int employeeId) {
        List<Leaves> leaves = userService.getEmployeeLeaves(employeeId);
        if (leaves == null || leaves.isEmpty()) {
            throw new UserNotFoundException("Employee with ID '" + employeeId + "' not found or has no leave records");
        }
        return new ResponseEntity<>(leaves, HttpStatus.OK);
    }
    
    //get employees by department
    @GetMapping("/admin_getEmployeesByDepartment/{dId}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable int dId) {
        List<Employee> employees = userService.getEmployeesByDepartment(dId);
        if (employees.isEmpty()) {
            throw new UserNotFoundException("No employees found in department with ID '" + dId + "'");
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    
    //get all leaves of employees
    @GetMapping("/admin_getAllLeaves")
    public ResponseEntity<List<Leaves>> getAllLeaves() {
        List<Leaves> leaves = userService.getAllLeaves();
        if (leaves.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(leaves);
    }

    //update employee salary
    @PutMapping("/admin_updateEmployeeSalary/{empId}")
    public ResponseEntity<Employee> updateEmployeeSalary(@PathVariable int empId, @RequestParam double newSalary) {
    	Employee e = userService.updateEmployeeSalary(empId, newSalary);
        if (e == null) {
            throw new UserNotFoundException("Employee with ID '" + empId + "' not found");
        }
        return new ResponseEntity<>(e, HttpStatus.OK);
    }

    //approve, reject leaves by leave id
    @PutMapping("/admin_approveRejectLeave/{leaveId}")
    public ResponseEntity<Leaves> approveRejectLeaveRequest(@PathVariable int leaveId, @RequestParam boolean approved) {
        Leaves updatedLeave = userService.approveRejectLeaveRequest(leaveId, approved);
        if (updatedLeave == null) {
            throw new UserNotFoundException("Leave request with ID '" + leaveId + "' not found");
        }
        return new ResponseEntity<>(updatedLeave, HttpStatus.OK);
    }
    
    //update department id of employee
    @PutMapping("/admin_update_department/{id}")
    public ResponseEntity<String> updateEmployeeDepartment(@PathVariable int id, @RequestParam int departmentId) {
        try {
        	userService.updateEmployeeDepartment(id, departmentId);
            return ResponseEntity.ok("Employee department updated successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the department.");
        }
    }
    
    //remove user
    @DeleteMapping("/admin_removeUser/{userID}")
    public ResponseEntity<String> admin_removeUser(@PathVariable int userID) {
        String result = userService.admin_removeUser(userID);
        if (result.equals("User not found")) {
            throw new UserNotFoundException("User with ID '" + userID + "' not found");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    //remove employee
    @DeleteMapping("/admin_removeEmp/{empID}")
    public ResponseEntity<String> admin_removeEmp(@PathVariable int empID) {
        String result = userService.admin_removeEmp(empID);
        if (result.equals("Employee not found")) {
            throw new UserNotFoundException("Employee with ID '" + empID + "' not found");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
