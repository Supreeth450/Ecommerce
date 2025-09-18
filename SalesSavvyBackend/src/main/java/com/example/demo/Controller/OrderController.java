package com.example.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.users;
import com.example.demo.Services.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	
	@GetMapping
	public ResponseEntity<Map<String,Object>> getOrderForUser(HttpServletRequest request) {
		try {
			users authUser = (users) request.getAttribute("authenticatedUser");
			
			if(authUser==null) {
				return ResponseEntity.status(401).body(Map.of("error","User not authenticated"));
			}
			
			Map<String,Object> response = orderService.getOrdersForUser(authUser);
			
			return ResponseEntity.ok(response);
		}
		catch(IllegalArgumentException e) {
			return ResponseEntity.status(400).body(Map.of("error",e.getMessage()));
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error","An unexpected error occurred"));
		}
	}
	

}
