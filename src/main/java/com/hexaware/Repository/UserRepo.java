package com.hexaware.Repository;

import com.hexaware.Entity.Users;

import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {
	
	@Query("SELECT u FROM Users u WHERE u.username = :username")
	Users findByUsername(@Param("username") String username);
	
	@Query("Select count(u) from Users u where u.role='EMPLOYEE'")
    long countTotalEmployees();
	
	@Query("Select count(u) from Users u where u.role='EMPLOYEE'")
    long countTotalPayrollManager();
	
	@Query("Select count(u) from Users u where u.role='EMPLOYEE'")
    long countTotalAdmin();
	
	@Query("Select u from Users u where u.username=?1")
	Users getUserByUsername(String name);
}