package com.example.demo.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5174",allowCredentials = "true")
public class CartController {
	
	
	@PostMapping("/add")
	public ResponseEntity<Void> addToCart(@RequestBody Map<String, Object> request){
		
		
		return null;
	}

}
