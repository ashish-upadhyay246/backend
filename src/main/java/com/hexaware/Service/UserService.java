package com.hexaware.Service;

import com.hexaware.Entity.Department;
import com.hexaware.Entity.Employee;
import com.hexaware.Entity.Leaves;
import com.hexaware.Entity.Leaves.LeaveStatus;
import com.hexaware.Entity.Users;
import com.hexaware.Entity.Users.Role;
import com.hexaware.Exceptions.EmployeeCustomExceptions.EmployeeNotFoundException;
import com.hexaware.Repository.DepartmentRepo;
import com.hexaware.Repository.EmployeeRepo;
import com.hexaware.Repository.LeaveRepo;
import com.hexaware.Repository.PayrollRepo;
import com.hexaware.Repository.UserRepo;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService{

    @Autowired UserRepo userRepo;
    @Autowired EmployeeRepo employeeRepo;
    @Autowired DepartmentRepo departmentRepo;
    @Autowired LeaveRepo leaveRepo;
    @Autowired PayrollRepo payrollRepo;
    @Autowired JWTService service;
	@Autowired AuthenticationManager authManager;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    //register user
    public Users admin_addUser(Users u) {
        Users user = new Users(u.getUsername(), u.getPassword(), u.getRole());
        user.setPassword(encoder.encode(user.getPassword()));
        Users savedUser = userRepo.save(user);
        return savedUser;
    }
    
    //verify at login
    public String verify(Users u) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword()));
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return service.generateToken(userDetails);
        }
        throw new RuntimeException("Authentication failed");
    }

    //edit user name
    public String admin_editUserName(String uname, String newname) {
        Users user = userRepo.findByUsername(uname);
        if (user == null) {
            return "User not found";
        }
        if (userRepo.findByUsername(newname) != null) {
            return "Username already exists";
        }
        user.setUsername(newname);
        userRepo.save(user);
        return "Username updated.";
    }

    //edit user role
    public String admin_editUserRole(String currentUsername, Role newRole) {
        Users user = userRepo.findByUsername(currentUsername);
        if (user == null) {
            return "User not found";
        }
        user.setRole(newRole);
        userRepo.save(user);
        return "User role updated.";
    }

    //edit user password
    public String admin_editUserPwd(String currentUsername, String pwd) {
        Users user = userRepo.findByUsername(currentUsername);
        if (user == null) {
            return "User not found";
        }
        user.setPassword(encoder.encode(pwd));
        userRepo.save(user);
        return "User password updated.";
    }
    
    //add an employee
    public Employee createEmployee(Employee e, int dId, int uId) {
    	Users u=userRepo.findByUserId(uId);
        Department d=departmentRepo.findByDeptId(dId);
        System.out.println(d);
        e.setUser(u);
        e.setDepartment(d);
        return employeeRepo.save(e);
    }
    
    //get all users
    public List<Users> admin_showAll() {
        return userRepo.findAll();
    }
    
    //get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    //get specific user
    public Users admin_getDataById(int userID) {
        Users user = userRepo.findByUserId(userID);
        return user;
    }
    
    //get specific employee's leaves
    public List<Leaves> getEmployeeLeaves(int employeeId) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if (employee == null) {
            throw new RuntimeException("Employee not found");
        }
        return leaveRepo.findByEmployee(employee.getEmpId());
    }
    
    //get employees by department
    public List<Employee> getEmployeesByDepartment(int dId) {
        Department department = departmentRepo.findById(dId).orElseThrow(() -> new RuntimeException("Department not found"));
        int d=department.getDeptId();
        List<Employee> employees = employeeRepo.findByDepartment(d);
        return employees;
    }
    
    //get all leaves of employees
    public List<Leaves> getAllLeaves() {
        return leaveRepo.findAll();
    }
    
    //update employee salary
    public Employee updateEmployeeSalary(int empId, double newSalary) {
        Employee e = employeeRepo.findById(empId).orElse(null);
        if (e == null) {
            throw new RuntimeException("Employee not found");
        }
        e.setSalary(newSalary);
        Employee updatedEmployee = employeeRepo.save(e);
        return updatedEmployee;
    }
    
    //approve, reject leaves by leave id
    public Leaves approveRejectLeaveRequest(int leaveId, boolean approved) {
        Leaves leave = leaveRepo.findById(leaveId).orElse(null);
        if (leave == null) {
            throw new RuntimeException("Leave request not found");
        }
        Employee employee = leave.getEmployee();
        if (approved) {
            leave.setStatus(LeaveStatus.APPROVED);
            employee.setLeavesLeft(employee.getLeavesLeft() - leave.getDays());
        } else {
            leave.setStatus(LeaveStatus.REJECTED);
        }
        employeeRepo.save(employee);
        return leaveRepo.save(leave);
    }

    //update department of employee
    public void updateEmployeeDepartment(int employeeId, int departmentId) {
        Employee employee = employeeRepo.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee with ID " + employeeId + " not found."));
        Department department = departmentRepo.findById(departmentId).orElseThrow(() -> new EntityNotFoundException("Department with ID " + departmentId + " not found."));
        employee.setDepartment(department);
        employeeRepo.save(employee);
    }

    //remove user
    public String admin_removeUser(int userID) {
        Users user = userRepo.findById(userID).orElse(null);
        if (user == null) {
            return "User does not exist.";
        }
        userRepo.deleteById(userID);
        return "User removed successfully";
    }
    
    public String getRole(String username) {
        Users user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.getRole().name();
    }
        
    public Users getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepo.findByUsername(username);
    }
    
	public Users getUserByUsername(String name) {
		return userRepo.getUserByUsername(name);
	}

	//remove employee
    public String admin_removeEmp(int empID) {
        Employee e = employeeRepo.findById(empID).orElse(null);
        leaveRepo.deleteByEmployee(e);
        payrollRepo.deleteByEmployee(e);
        if (e != null) {
            employeeRepo.deleteById(empID);
            return "Employee removed successfully.";
        }
        else{
        	throw new EmployeeNotFoundException("Employee with ID: "+empID+" does not exist!");
        }
    }
}