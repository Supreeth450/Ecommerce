package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.JWTToken;

@Repository
public interface JWTTokenRepository extends JpaRepository<JWTToken, Integer>{
	
	
	@Query("SELECT t FROM JWTToken t WHERE t.user.userId = :userId")
	JWTToken findByuserId(@Param("userId") int userId);

}
