package com.example.demo.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.CartItem;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.users;
import com.example.demo.Repositories.CartRepository;
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
}
