package com.example.demo.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="jwt_tokens")
public class JWTToken {
	
	
	private int token_id;
	
	
	private users user;
	
	private String token;
	
	private LocalDateTime expiresAt;
	
	
	
	

}
