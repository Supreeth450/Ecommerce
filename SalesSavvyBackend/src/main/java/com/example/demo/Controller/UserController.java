package com.example.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.users;
import com.example.demo.Services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	
	 private final UserService userservice;

	 @Autowired
	 public UserController(UserService userservice) {
		super();
		this.userservice = userservice;
	 }
	 
	 @PostMapping("/register")
	 public ResponseEntity<?> registerUser(@RequestBody users user){
		 try {
			 users registeredUser = userservice.registerUser(user);
			 return ResponseEntity.ok(Map.of("message","User registered successfully","user",registeredUser));
		 }
		 catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	 }
}
