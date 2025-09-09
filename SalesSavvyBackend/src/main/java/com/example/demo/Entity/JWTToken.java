package com.example.demo.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="jwt_tokens")
public class JWTToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int token_id;
	
	@ManyToOne
	@JoinColumn(name = "user_id",nullable = false)
	private users user;
	
	@Column(nullable = false)
	private String token;
	
	@Column(name = "expires_at")
	private LocalDateTime expiresAt;

	public JWTToken() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JWTToken(users user, String token, LocalDateTime expiresAt) {
		super();
		this.user = user;
		this.token = token;
		this.expiresAt = expiresAt;
	}

	public JWTToken(int token_id, users user, String token, LocalDateTime expiresAt) {
		super();
		this.token_id = token_id;
		this.user = user;
		this.token = token;
		this.expiresAt = expiresAt;
	}

	public int getToken_id() {
		return token_id;
	}

	public void setToken_id(int token_id) {
		this.token_id = token_id;
	}

	public users getUser() {
		return user;
	}

	public void setUser(users user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
	
	
	
	
	
	

}
