package com.example.demo.Services;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.users;
import com.example.demo.Repositories.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepo;
	private final BCryptPasswordEncoder passwordEncoder;
	
	
    @Autowired
	public UserService(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
		this.passwordEncoder = new BCryptPasswordEncoder();
		
	}



	public users registerUser(users user) {
		if(userRepo.findByuserName(user.getUserName()).isPresent()) {
			throw new RuntimeException("Username aready Exists");
		}
		
		if(userRepo.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email aready Exists");
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return userRepo.save(user);
	}

}
