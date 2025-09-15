package com.example.demo.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.CartItem;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Entity.users;
import com.example.demo.Repositories.CartRepository;
import com.example.demo.Repositories.ProductImageRepository;
import com.example.demo.Repositories.ProductRepository;
import com.example.demo.Repositories.UserRepository;

@Service
public class CartService {
           
	@Autowired
	CartRepository cartRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ProductRepository prodRepo;
	
	@Autowired
	ProductImageRepository prodImageRepo;
	
	public void addToCart(int userId,int productId, int quantity) {
		users user = userRepo.findById(userId).orElseThrow(()-> new RuntimeException("user id not found"));
		
		Product product = prodRepo.findById(productId).orElseThrow(()-> new RuntimeException("product id not found"));
		
		Optional<CartItem> existingItem = cartRepo.findByusersAndProduct(userId,productId);
		
		if(existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			cartItem.setQuantity(cartItem.getQuantity()+quantity);
			cartRepo.save(cartItem);
		}
		else {
			CartItem newItem = new CartItem(user,product,quantity);
			cartRepo.save(newItem);
		}
		
		
		
	}

	public int countbyId(int userId) {
		int count = cartRepo.getCartItemCount(userId);
		return count;
	}

	public Map<String, Object> getCartItems(int user_id) {
	   
		List<CartItem> cartItems = cartRepo.findCartItemsWithProductDetails(user_id);
		
		Map<String,Object> response = new HashMap<>();
		
		users user = userRepo.findById(user_id).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
		
		response.put("username", user.getUserName());
		response.put("role", user.getRole().toString());
		
		List<Map<String,Object>> products = new ArrayList<>();
		int overallTotalPrice = 0;
		
		for(CartItem cartItem : cartItems) {
			Map<String,Object> productDetails = new HashMap<>();
			
			Product product = cartItem.getProduct();
			
			List<ProductImage> productImages = prodImageRepo.findByProduct_ProductId(product.getProductId());
			 
			String imageUrl = (productImages != null && !productImages.isEmpty()) ? productImages.get(0).getImageUrl() : "default-image-url";
			
			productDetails.put("product_id", product.getProductId());
			productDetails.put("image_url", imageUrl);
			productDetails.put("name", product.getName());
			productDetails.put("description", product.getDescription());
			productDetails.put("price_per_unit", product.getPrice());
			productDetails.put("quantity", cartItem.getQuantity());
			productDetails.put("total_price", cartItem.getQuantity() * product.getPrice().doubleValue());
			
			products.add(productDetails);
			
			overallTotalPrice += cartItem.getQuantity() * product.getPrice().doubleValue();
			
		}
		
		Map<String,Object> cart = new HashMap<>();
		cart.put("products", products);
		cart.put("overall_total_price", overallTotalPrice);
		
		response.put("cart", cart);
		
		return response;
		
	}

	public void updateCartItemQuantity(int user_id,int product_id,int quantity) {
		
		Product product = prodRepo.findById(product_id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
		
		Optional<CartItem> existingItem = cartRepo.findByusersAndProduct(user_id, product_id);
		
		if(existingItem.isPresent()) {
			if(quantity == 0) {
				deleteCartItem(user_id,product_id);
			} else {
		        CartItem cartitem = existingItem.get();
		        cartitem.setQuantity(quantity);
		        cartRepo.save(cartitem);
			}
		}
	}

	public void deleteCartItem(int user_id, int product_id) {
		Product product = prodRepo.findById(product_id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
		
		cartRepo.deleteCartItem(user_id,product_id);
		
	}
	
	
}
