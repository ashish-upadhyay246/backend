package com.hexaware.Repository;

import com.hexaware.Entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {
	
	@Query("SELECT u FROM Users u WHERE u.username = :username")
	Users findByUsername(@Param("username") String username);
	
	@Query("select u from Users u where u.userId=:user_id")
	Users findByUserId(@Param("user_id") int user_id);
	
	@Query("Select u from Users u where u.username=?1")
	Users getUserByUsername(String name);
}