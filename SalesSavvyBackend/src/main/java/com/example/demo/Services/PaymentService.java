package com.example.demo.Services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.Order;
import com.example.demo.Entity.OrderItem;
import com.example.demo.Entity.OrderStatus;
import com.example.demo.Repositories.CartRepository;
import com.example.demo.Repositories.OrderItemRepository;
import com.example.demo.Repositories.OrderRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;



@Service
public class PaymentService {
	
	@Value("${razorpay.key_id}")
	private String razorpayKeyId;
	
	@Value("${razorpay.key_secret}")
	private String razorpayKeySecret;
	
	
	private final OrderRepository orderRepo;
	private final CartRepository cartRepo;
	private final OrderItemRepository orderItemRepo;
	
	
   
	public PaymentService(OrderRepository orderRepo, CartRepository cartRepo, OrderItemRepository orderItemRepo) {
		super();
		this.orderRepo = orderRepo;
		this.cartRepo = cartRepo;
		this.orderItemRepo = orderItemRepo;
	}


	 @Transactional
	public String createOrder(int user_id, BigDecimal totalAmount, List<OrderItem> cartItems) throws RazorpayException{
		
		 RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
		 
		 var orderRequest = new JSONObject();
		 orderRequest.put("amount", totalAmount.multiply(BigDecimal.valueOf(100)).intValue());
		 orderRequest.put("currency", "INR");
		 orderRequest.put("receipt", "txn_"+System.currentTimeMillis());
		 
		 com.razorpay.Order razorpayOrder= razorpayClient.orders.create(orderRequest);
		 
		 Order order = new Order();
		 order.setOrderId(razorpayOrder.get("id"));
		 order.setUserId(user_id);
		 order.setTotalAmount(totalAmount);
		 order.setStatus(OrderStatus.PENDING);
		 order.setCreatedAt(LocalDateTime.now());
		 orderRepo.save(order);
		 
		 return razorpayOrder.get("id");
	}
	
	

}
