package com.hexaware.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hexaware.Entity.Employee;
import com.hexaware.Entity.Leaves;

public interface LeaveRepo extends JpaRepository<Leaves, Integer> {

	@Query("select l from Leaves l where l.employee.empId = :employeeId")
    List<Leaves> findByEmployee(int employeeId);

	void deleteByEmployee(Employee e);
}