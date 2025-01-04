package com.hexaware.Repository;

import com.hexaware.Entity.Employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
    
    @Query("select e from Employee e where e.department.deptId = :departmentId")
    List<Employee> findByDepartment(@Param("departmentId") int departmentId);

    @Query("select e from Employee e where e.firstName = :firstName")
    Optional<Employee> findByFirstName(@Param("firstName") String firstName);

    @Query("select e from Employee e where e.user.userId=:id")
	Employee findByEmpId(int id);
}