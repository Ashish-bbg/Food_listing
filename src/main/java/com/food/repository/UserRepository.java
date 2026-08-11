package com.food.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.entity.User;



@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
	
	boolean existsByEmail(String email);
	
	boolean existsByPhone(String phone);
	
	boolean existsByEmailAndIdNot(String email, UUID currentUserId);
	
	boolean existsByPhoneAndIdNot(String phone, UUID curretUserId);
	
	
	Optional<User> findByEmail(String email);

}
