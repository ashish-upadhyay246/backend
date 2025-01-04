package com.hexaware.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.Entity.Department;
import com.hexaware.Repository.DepartmentRepo;

@Service
public class DepartmentService {

	@Autowired DepartmentRepo deptRepo;

	//add department
	public Department add(Department d) {
		return deptRepo.save(d);
	}
	
	
}
