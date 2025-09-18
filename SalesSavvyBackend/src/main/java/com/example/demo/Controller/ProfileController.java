package com.example.demo.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.users;
import com.example.demo.Repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
public class ProfileController {
	
	@Autowired
	private UserRepository userRepo;
	
	
	@GetMapping("/info")
	public ResponseEntity<Map<String,Object>> getProfileInfo(HttpServletRequest request) {
		users authUser = (users) request.getAttribute("authenticatedUser");
		
		if(authUser==null ) {
			return ResponseEntity.status(401).body(Map.of("error","Unauthorized access"));
		}
		
		users user = userRepo.findByuserName(authUser.getUserName()).orElseThrow(() -> new IllegalArgumentException("usernot found with username:" +authUser.getUserName()));
		
		Map<String,Object> userInfo = new HashMap<>();
		
		userInfo.put("user_name", user.getUserName());
		userInfo.put("email", user.getEmail());
		userInfo.put("role", user.getRole().toString());
		userInfo.put("created_at", user.getCreated_at());
		
		return ResponseEntity.ok(userInfo);
		
	}
	

}
