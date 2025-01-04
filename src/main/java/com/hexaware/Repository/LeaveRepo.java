package com.hexaware.Repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.hexaware.Entity.Leaves;

public interface LeaveRepo extends JpaRepository<Leaves, Integer> {

	 @Query(value = "SELECT * FROM Leaves WHERE emp_id = :employeeId", nativeQuery = true)
     List<Leaves> findByEmployee(@Param("employeeId") int employeeId);
	
	 List<Leaves> findByEmployee_EmpId(int empId);

	
}
