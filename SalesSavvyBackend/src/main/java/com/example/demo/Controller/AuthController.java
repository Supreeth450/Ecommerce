package com.example.demo.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.LoginRequest;
import com.example.demo.Entity.users;
import com.example.demo.Services.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {
          
	AuthService authService;
	
	
	public AuthController(AuthService authService) {
		super();
		this.authService = authService;
	}


	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,HttpServletResponse response){
		
		try {
			users user = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
			
			String token = authService.generateToken(user);
			
			Cookie cookie = new Cookie("authToken",token);
			cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            cookie.setDomain("localhost");
            response.addCookie(cookie);
            
         response.addHeader("Set-Cookie", String.format("authToken=%s ; HttpOnly; Path=/; Max-Age=3600; SameSite=None",token));
         
         
         Map<String,Object> responseBody = new HashMap<>();
         responseBody.put("message", "Login Successful");
         responseBody.put("role", user.getRole().name());
         responseBody.put("UserEmail", user.getEmail());
			
         
         return ResponseEntity.ok(responseBody);
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error",e.getMessage()));
		}
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Map<String,String>> logout(HttpServletRequest request,HttpServletResponse response) {
		try {
			users user = (users) request.getAttribute("authenticatedUser");
			
			authService.logout(user);
			
			Cookie cookie = new Cookie("authToken",null);
			cookie.setHttpOnly(true);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
			
			Map<String,String> responseBody = new HashMap<>();
			responseBody.put("message", "Logout Successful");
			return ResponseEntity.ok(responseBody);
			
		}
		catch(Exception e) {
			Map<String,String> errorResponse = new HashMap<>();
			errorResponse.put("message", "Logout failed");
			return ResponseEntity.status(500).body(errorResponse);
		}
	}
	
	
}
