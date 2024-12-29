package com.hexaware.Service;

import com.hexaware.DTO.EmployeeDTO;

import com.hexaware.DTO.UserDTO;
import com.hexaware.Entity.Department;
import com.hexaware.Entity.Employee;
import com.hexaware.Entity.Leaves;
import com.hexaware.Entity.Users;
import com.hexaware.Entity.Users.Role;
import com.hexaware.Repository.DepartmentRepo;
import com.hexaware.Repository.EmployeeRepo;
import com.hexaware.Repository.LeaveRepo;
import com.hexaware.Repository.UserRepo;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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

    @Autowired
    UserRepo rep;
    
    @Autowired
    EmployeeRepo employeeRepo;
    
    @Autowired
    DepartmentRepo departmentRepo;
    
    @Autowired
    LeaveRepo leaveRepo;
    
    @Autowired
	JWTService service;
    
    @Autowired
    private ModelMapper mp;
	//comment
	@Autowired
	AuthenticationManager authManager;
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    //(admin) register
    public Users admin_addUser(UserDTO userDTO) {
        Users user = new Users(userDTO.getUsername(), userDTO.getPassword(), Users.Role.valueOf(userDTO.getRole()));
        user.setPassword(encoder.encode(user.getPassword()));
        Users savedUser = rep.save(user);
        return savedUser;
    }

    //edit user name
    public String admin_editUserName(String currentUsername, String newUsername) {
        Users user = rep.findByUsername(currentUsername);
        if (user == null) {
            return "User not found";
        }
        if (rep.findByUsername(newUsername) != null) {
            return "Username already exists";
        }
        user.setUsername(newUsername);
        rep.save(user);
        return "Username updated.";
    }

    //edit user role
    public String admin_editUserRole(String currentUsername, Role newRole) {
        Users user = rep.findByUsername(currentUsername);
        if (user == null) {
            return "User not found";
        }
        user.setRole(newRole);
        rep.save(user);
        return "User role updated.";
    }

    //edit user password
    public String admin_editUserPwd(String currentUsername, String pwd) {
        Users user = rep.findByUsername(currentUsername);
        if (user == null) {
            return "User not found";
        }
        user.setPassword(encoder.encode(pwd));
        rep.save(user);
        return "User password updated.";
    }
    
    //add an employee
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        
        Employee employee = mp.map(employeeDTO, Employee.class);
        Department d=departmentRepo.findByDeptId(employeeDTO.getDepartmentId());
        employee.setDepartment(d);
        Employee savedEmployee = employeeRepo.save(employee);

        return mp.map(savedEmployee, EmployeeDTO.class);
    }
    
    

    //get all users
    public List<Users> admin_showAll() {
        return rep.findAll();
    }
    
    //get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    //get specific user
    public Users admin_getDataById(int userID) {
        Users user = rep.findByUserId(userID);
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
    public List<Employee> getEmployeesByDepartment(int departmentId) {
        Department department = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        List<Employee> employees = employeeRepo.findByDepartment(department.getDeptId());
        return employees;
    }
    
    //get all leaves of employees
    public List<Leaves> getAllLeaves() {
        return leaveRepo.findAll();
    }
    
    //get total number of employees
    public long getTotalEmployees() {
        return rep.countTotalEmployees();
    }
    
    //get total number of payroll managers
    public long getTotalPayrollManager() {
        return rep.countTotalPayrollManager();
    }
    
    //get total number of admins
    public long getTotalAdmin() {
        return rep.countTotalAdmin();
    }
    
    //update employee salary
    public Employee updateEmployeeSalary(int employeeId, double newSalary) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if (employee == null) {
            throw new RuntimeException("Employee not found");
        }
        employee.setSalary(newSalary);
        Employee updatedEmployee = employeeRepo.save(employee);
        return updatedEmployee;
    }
    
    //approve, reject leaves by leave id
    public Leaves approveRejectLeaveRequest(int leaveId, boolean approved) {
        Leaves leave = leaveRepo.findById(leaveId).orElse(null);
        if (leave == null) {
            throw new RuntimeException("Leave request not found");
        }
        if (approved) {
            leave.approve(); 
        } else {
            leave.reject(); 
        }

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
        Users user = rep.findById(userID).orElse(null);
        if (user == null) {
            return "User does not exist.";
        }
        rep.deleteById(userID);
        return "User removed successfully";
    }

    //Verify
    public String verify(Users u) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword())
        );
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return service.generateToken(userDetails);
        }

        throw new RuntimeException("Authentication failed");
    }
    
    public String getRole(String username) {
        Users user = rep.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.getRole().name();
    }
        
    public Users getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return rep.findByUsername(username);
    }
    
	public Users getUserByUsername(String name) {
		return rep.getUserByUsername(name);

	}

	public Department getDept(int id) {
		
		return departmentRepo.findByDeptId(id);
	}
}