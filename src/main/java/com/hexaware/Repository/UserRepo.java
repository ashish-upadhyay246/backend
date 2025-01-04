package com.hexaware.Repository;

import com.hexaware.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {
	
	@Query("select u from Users u where u.username = :username")
	Users findByUsername(String username);
	
	@Query("select u from Users u where u.userId=:user_id")
	Users findByUserId(int user_id);
	
	@Query("Select u from Users u where u.username=?1")
	Users getUserByUsername(String name);
}