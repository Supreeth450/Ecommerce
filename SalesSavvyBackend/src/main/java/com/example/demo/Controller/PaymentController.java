package com.example.demo.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.OrderItem;
import com.example.demo.Entity.users;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.PaymentService;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("api/payment")
public class PaymentController {
      
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private UserRepository userRepo;
	
	@PostMapping("/create")
	public ResponseEntity<String> createPaymentOrder(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
		try {
			users user = (users) request.getAttribute("authenticatedUser");
			
			if(user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
			}
			
			
			BigDecimal totalAmount = new BigDecimal(requestBody.get("totalAmount").toString());
			
			List<Map<String, Object>> cartItemsRaw = (List<Map<String,Object>>) requestBody.get("cartItems");
			
			List<OrderItem> cartItems = cartItemsRaw.stream().map(item ->{
				OrderItem orderItem = new OrderItem();
				orderItem.setProductId((Integer)item.get("productId"));
				orderItem.setQuantity((Integer)item.get("quantity"));
				BigDecimal pricePerUnit = new BigDecimal(item.get("price").toString());
				orderItem.setPricePerCount(pricePerUnit);
				orderItem.setTotalPrice(pricePerUnit.multiply(BigDecimal.valueOf((Integer)item.get("quantity"))));			
				return orderItem;
			}).collect(Collectors.toList());
			
			
			String razorPayOrderId = paymentService.createOrder(user.getUser_id(),totalAmount,cartItems);
			
			return ResponseEntity.ok(razorPayOrderId);
		}
		catch(RazorpayException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating  Razorpay Order: " + e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data: " + e.getMessage());
		}
	}
	
	@PostMapping("/verify")
	public ResponseEntity<String> verifyPayment(@RequestBody Map<String,Object> requestBody, HttpServletRequest request) {
		try {
			users user = (users) request.getAttribute("authenticatedUser");
			
			if(user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
			}
			
			int userId = user.getUser_id();
			
			String razorpayOrderId = (String) requestBody.get("razorpayOrderId");
			String razorpayPaymentId = (String) requestBody.get("razorpayPaymentId");
            String razorpaySignature = (String) requestBody.get("razorpaySignature");
            
            boolean isVerified = paymentService.verifyPayment(razorpayOrderId,razorpayPaymentId,razorpaySignature,userId);
            
            if(isVerified) {
            	return ResponseEntity.ok("Payment verified successfully");
            }
            else {
            	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment verification failed");
            }
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying payment: " +e.getMessage());
		}
	}	
	
}
