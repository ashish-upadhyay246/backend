package com.hexaware.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.Entity.Department;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Integer> {

	Department findByDeptId(int deptId);

}