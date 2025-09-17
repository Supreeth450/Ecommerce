package com.example.demo.Services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.CartItem;
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

     @Transactional
	 public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature,int userId) {
		try {
		JSONObject attributes = new JSONObject();
		attributes.put("razorpay_order_id", razorpayOrderId);
		attributes.put("razorpay_payment_id", razorpayPaymentId);
		attributes.put("razorpay_signature", razorpaySignature);
		
		boolean isSignatureValid = com.razorpay.Utils.verifyPaymentSignature(attributes, razorpayKeySecret);
		
		if(isSignatureValid) {
			Order order = orderRepo.findById(razorpayOrderId).orElseThrow(() -> new RuntimeException("Order not found"));
			order.setStatus(OrderStatus.SUCCESS);
			order.setUpdatedAt(LocalDateTime.now());
			orderRepo.save(order);
			
			List<CartItem> cartItems = cartRepo.findCartItemsWithProductDetails(userId);
			
			for(CartItem cartItem : cartItems) {
				OrderItem orderItem = new OrderItem();
				orderItem.setOrder(order);
				orderItem.setProductId(cartItem.getProduct().getProductId());
				orderItem.setQuantity(cartItem.getQuantity());
				orderItem.setPricePerCount(cartItem.getProduct().getPrice());
				
				orderItem.setTotalPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
				orderItemRepo.save(orderItem);
				
				cartRepo.deleteAllCartItemsByUserId(userId);
							
			}
			return true;
		}
		
		else {
			return false;
		}
		
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	 }
	
	

}
