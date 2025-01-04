package com.hexaware.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.Entity.Department;
import com.hexaware.Service.DepartmentService;

@RestController
@RequestMapping("/api/department")
@CrossOrigin("http://localhost:3000")
public class DepartmentController {
	
	@Autowired DepartmentService deptService;
	
	//add department
	@PostMapping("/add")
	public Department add(@RequestBody Department d) {
		return deptService.add(d);
	}

}
