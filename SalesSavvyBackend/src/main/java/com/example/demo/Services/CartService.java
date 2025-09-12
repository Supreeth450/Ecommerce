package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		
	}
}
