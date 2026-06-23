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
	
	Optional<User> findByEmail(String email);

}
