package com.example.demo.AdminServices;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.users;
import com.example.demo.Repositories.JWTTokenRepository;
import com.example.demo.Repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminUserService {
	
	public final UserRepository userRepo;
	public final JWTTokenRepository tokenRepo;
	
	
   
	public AdminUserService(UserRepository userRepo, JWTTokenRepository tokenRepo) {
		super();
		this.userRepo = userRepo;
		this.tokenRepo = tokenRepo;
	}
    
	 @Transactional
	public users modifyUser(Integer userId, String username, String email, String role) {
		Optional<users> userOptional = userRepo.findById(userId);
		if(userOptional.isEmpty()) {
			throw new IllegalArgumentException("User not found");
		}
		
		users existingUser = userOptional.get();
		
		if(username!=null && !username.isEmpty()) {
			existingUser.setUserName(username);
		}
		
		if(email!=null && !email.isEmpty()) {
			existingUser.setEmail(email);
		}
		
		if(role!=null && !role.isEmpty()) {
			try {
				existingUser.setRole(Role.valueOf(role));
			}
			catch(IllegalArgumentException e) {
				throw new IllegalArgumentException("Invalid role: " + role);
			}
		}
		
		tokenRepo.deleteByUserId(userId);
		
		return userRepo.save(existingUser);
		
		
	}
	 

	public users getUserById(Integer userId) {
		return userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
	}

}
