package com.hexaware.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.Entity.Department;
import com.hexaware.Entity.Employee;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Integer> {

	Department findByDeptId(int deptId);

}