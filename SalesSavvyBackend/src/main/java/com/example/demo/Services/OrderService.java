package com.example.demo.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.OrderItem;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Entity.users;
import com.example.demo.Repositories.OrderItemRepository;
import com.example.demo.Repositories.ProductImageRepository;
import com.example.demo.Repositories.ProductRepository;


@Service
public class OrderService {
	
	@Autowired
	private OrderItemRepository orderItemRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductImageRepository productImageRepo;

	
	public Map<String, Object> getOrdersForUser(users authUser) {
		
		List<OrderItem> orderItems = orderItemRepo.findSuccessfulOrderItemsByUserId(authUser.getUser_id());
		
		Map<String,Object> response = new HashMap<>();
		response.put("username", authUser.getUserName());
		response.put("role", authUser.getRole());
		
		List<Map<String,Object>> products = new ArrayList<>();
		
		for(OrderItem item:orderItems) {
			Product product = productRepo.findById(item.getProductId()).orElse(null);
			
		    if(product == null) {
		    	continue;
		    }
		    
		    List<ProductImage> images = productImageRepo.findByProduct_ProductId(product.getProductId());
		    String imageUrl = images.isEmpty() ? null:images.get(0).getImageUrl();
		    
		    Map<String,Object> productDetails = new HashMap<>();
		    productDetails.put("order_id", item.getOrder().getOrderId());
		    productDetails.put("quantity", item.getQuantity());
		    productDetails.put("total_price", item.getTotalPrice());
		    productDetails.put("image_url", imageUrl);
		    productDetails.put("product_id", product.getProductId());
		    productDetails.put("product_name", product.getName());
		    productDetails.put("description", product.getDescription());
		    productDetails.put("price_per_unit", item.getPricePerCount());
		    
		    products.add(productDetails);
		    
		}
		
		response.put("products", products);
		
		return response;
	}

}
