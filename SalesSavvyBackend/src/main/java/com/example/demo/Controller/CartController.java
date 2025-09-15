package com.example.demo.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.users;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.CartService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
public class CartController {
	
	@Autowired
	CartService cartService;
	
	@Autowired
	UserRepository userRepo;
	
	@PostMapping("/add")
	public ResponseEntity<Void> addToCart(@RequestBody Map<String, Object> request){
		String userName = (String) request.get("username");
		
		int productId = (int) request.get("productId");
		
		int quantity = request.containsKey("quantity") ? (int) request.get("quantity") : 1;
		
		users user = userRepo.findByuserName(userName).orElseThrow(() -> new IllegalArgumentException("usernot found with username:" +userName));
		
		cartService.addToCart(user.getUser_id(), productId, quantity);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("/items/count")
	public ResponseEntity<Integer> getCartItemCount(@RequestParam String username) {
		users user = userRepo.findByuserName(username).orElseThrow(() -> new IllegalArgumentException("user not found with user name"));
		
		int count = cartService.countbyId(user.getUser_id());
		
		return ResponseEntity.ok(count);
	}
	
	@GetMapping("/items")
	public ResponseEntity<Map<String,Object>> getCartItems(HttpServletRequest request) {
		users user = (users) request.getAttribute("authenticatedUser");
		
		Map<String,Object> cartItems= cartService.getCartItems(user.getUser_id());
		
		return ResponseEntity.ok(cartItems);
	}
	
	
	
	
	

}
