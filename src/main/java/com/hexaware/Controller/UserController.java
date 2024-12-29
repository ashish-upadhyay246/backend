package com.hexaware.Controller;

import com.hexaware.DTO.EmployeeDTO;

import com.hexaware.DTO.UserDTO;
import com.hexaware.Entity.Department;
import com.hexaware.Entity.Employee;
import com.hexaware.Entity.Leaves;
import com.hexaware.Entity.Users;
import com.hexaware.Exceptions.UserCustomExceptions.DuplicateUsernameException;
import com.hexaware.Exceptions.UserCustomExceptions.UserNotFoundException;
import com.hexaware.Service.UserService;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.relation.RoleNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    UserService ser;
    
    @Autowired
    private ModelMapper mp;

    //(admin) register
    @PostMapping("/register")
    public ResponseEntity<UserDTO> admin_addUser(@RequestBody UserDTO userDTO) {
        Users userObj = ser.admin_addUser(userDTO); 
        UserDTO uDTO=mp.map(userObj, UserDTO.class);
        return new ResponseEntity<>(uDTO, HttpStatus.CREATED);
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Users u) {
        String token = ser.verify(u);
        String role = ser.getRole(u.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("jwt", token);
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    //edit user name
    @PostMapping("/admin_editUsername/{username}/{newname}")
    public ResponseEntity<String> admin_editUserName(@PathVariable String username, @PathVariable String newname) {
        String result = ser.admin_editUserName(username, newname);
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
            String result = ser.admin_editUserRole(username, newRole);
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
        String result = ser.admin_editUserPwd(username, pwd);
        if (result.equals("User not found")) {
            throw new UserNotFoundException("User with username '" + username + "' not found");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    //add an employee
    @PostMapping("/admin_addEmployeeDetails")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO newEmployeeDTO = ser.createEmployee(employeeDTO);
        return new ResponseEntity<>(newEmployeeDTO, HttpStatus.CREATED);
    }

    //get all users
    @GetMapping("/admin_getUserData")
    public ResponseEntity<List<UserDTO>> getData() {
    	List<Users> userObj = ser.admin_showAll();
        List<UserDTO> userDTOs= new ArrayList<>();
        for(Users u : userObj)
    	{
    		UserDTO x = mp.map(u, UserDTO.class);
    		userDTOs.add(x);
    	}
    	if(userDTOs.isEmpty()) {
    		return new ResponseEntity<List<UserDTO>>(userDTOs,HttpStatus.NO_CONTENT);
    	}
    	return new ResponseEntity<List<UserDTO>>(userDTOs,HttpStatus.OK);
    }
    
    //get all employees
    @GetMapping("/admin_getAllEmployees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<Employee> empObj = ser.getAllEmployees();
        List<EmployeeDTO> empDTOs= new ArrayList<>();
        for(Employee e : empObj)
    	{
    		EmployeeDTO x = mp.map(e, EmployeeDTO.class);
    		empDTOs.add(x);
    	}
    	if(empDTOs.isEmpty()) {
    		return new ResponseEntity<List<EmployeeDTO>>(empDTOs,HttpStatus.NO_CONTENT);
    	}
    	return new ResponseEntity<List<EmployeeDTO>>(empDTOs,HttpStatus.OK);
    }

    //get specific user
    @GetMapping("/admin_getUserData/{userID}")
    public ResponseEntity<?> getById(@PathVariable int userID) {
        Users userObj = ser.admin_getDataById(userID);
        System.out.println(userObj);
        if (userObj != null) {
        	UserDTO x=mp.map(userObj, UserDTO.class);
        	return new ResponseEntity<>(userObj, HttpStatus.OK);
        }
        else {
        	throw new UserNotFoundException("User with ID '" + userID + "' not found");
        }
    }
    
    //get specific employee's leaves
    @GetMapping("/admin_getEmployeeLeaves/{employeeId}")
    public ResponseEntity<List<Leaves>> getEmployeeLeaves(@PathVariable int employeeId) {
        List<Leaves> leaves = ser.getEmployeeLeaves(employeeId);
        if (leaves == null || leaves.isEmpty()) {
            throw new UserNotFoundException("Employee with ID '" + employeeId + "' not found or has no leave records");
        }
        return new ResponseEntity<>(leaves, HttpStatus.OK);
    }
    
    //get employees by department
    @GetMapping("/admin_getEmployeesByDepartment/{departmentId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(@PathVariable int departmentId) {
        List<Employee> employees = ser.getEmployeesByDepartment(departmentId);
        List<EmployeeDTO> empDTOs=new ArrayList<>();
        for(Employee e: employees)
        {
        	EmployeeDTO x=mp.map(e, EmployeeDTO.class);
        	empDTOs.add(x);
        }
        if (employees.isEmpty()) {
            throw new UserNotFoundException("No employees found in department with ID '" + departmentId + "'");
        }
        return new ResponseEntity<>(empDTOs, HttpStatus.OK);
    }
    
    //get all leaves of employees
    @GetMapping("/admin_getAllLeaves")
    public ResponseEntity<List<Leaves>> getAllLeaves() {
        List<Leaves> leaves = ser.getAllLeaves();
        if (leaves.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(leaves);
    }
    
    //get total employees number
    @GetMapping("/admin_total_employees")
    public long getTotalEmployees() {
        return ser.getTotalEmployees();
    }
    
    //get total payroll manager number
    @GetMapping("/admin_total_payroll_manager")
    public long getTotalPayrollManager() {
        return ser.getTotalPayrollManager();
    }
    
    //get total admin number
    @GetMapping("/admin_total_admin")
    public long getTotalAdmin() {
        return ser.getTotalAdmin();
    }    

    //update employee salary
    @PutMapping("/admin_updateEmployeeSalary/{employeeId}")
    public ResponseEntity<Employee> updateEmployeeSalary(@PathVariable int employeeId, @RequestParam double newSalary) {
    	Employee updateEmp = ser.updateEmployeeSalary(employeeId, newSalary);
        if (updateEmp == null) {
            throw new UserNotFoundException("Employee with ID '" + employeeId + "' not found");
        }
        EmployeeDTO empDTO=mp.map(updateEmp, EmployeeDTO.class);
        return new ResponseEntity<>(updateEmp, HttpStatus.OK);
    }

    //approve, reject leaves by leave id
    @PutMapping("/admin_approveRejectLeave/{leaveId}")
    public ResponseEntity<Leaves> approveRejectLeaveRequest(@PathVariable int leaveId, @RequestParam boolean approved) {
        Leaves updatedLeave = ser.approveRejectLeaveRequest(leaveId, approved);
        if (updatedLeave == null) {
            throw new UserNotFoundException("Leave request with ID '" + leaveId + "' not found");
        }
        return new ResponseEntity<>(updatedLeave, HttpStatus.OK);
    }
    
    //update department id of employee
    @PutMapping("/admin_update_department/{id}")
    public ResponseEntity<String> updateEmployeeDepartment(@PathVariable int id, @RequestParam int departmentId) {
        try {
            ser.updateEmployeeDepartment(id, departmentId);
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
        String result = ser.admin_removeUser(userID);
        if (result.equals("User not found")) {
            throw new UserNotFoundException("User with ID '" + userID + "' not found");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    //remove employee
    @DeleteMapping("/admin_removeEmp/{empID}")
    public ResponseEntity<String> admin_removeEmp(@PathVariable int empID) {
        String result = ser.admin_removeEmp(empID);
        if (result.equals("Employee not found")) {
            throw new UserNotFoundException("Employee with ID '" + empID + "' not found");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/dept/{id}")
    public ResponseEntity<?> fetchDept (@PathVariable int id) {
    	Department d = ser.getDept(id);
    	if (d!=null)
    	{
    		return new ResponseEntity<>(d, HttpStatus.OK);
    	}
    	else {
    		throw new UserNotFoundException("User with ID '" + id + "' not found");
    	}
    }
}
