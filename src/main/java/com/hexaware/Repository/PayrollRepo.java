package com.hexaware.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.Entity.Employee;
import com.hexaware.Entity.Payroll;

public interface PayrollRepo extends JpaRepository<Payroll, Integer>{

    List<Payroll> findByEmployee_EmpId(int employeeId);

	void deleteByEmployee(Employee e);
}