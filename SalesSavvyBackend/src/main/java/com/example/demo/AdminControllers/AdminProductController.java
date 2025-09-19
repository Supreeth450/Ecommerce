package com.example.demo.AdminControllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.AdminServices.AdminProductService;
import com.example.demo.Entity.Product;

@RestController
@RequestMapping("/admin/products")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminProductController {
	
	
	private final AdminProductService adminProdService;

	public AdminProductController(AdminProductService adminProdService) {
		super();
		this.adminProdService = adminProdService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addProduct(@RequestBody Map<String,Object> productRequest) {
		try {
			String name = (String) productRequest.get("name");
			String description = (String) productRequest.get("description");
			Double price = Double.valueOf(String.valueOf(productRequest.get("price")));
			Integer stock = (Integer) productRequest.get("stock");
			Integer categoryId = (Integer) productRequest.get("categoryId");
			String imageURL = (String) productRequest.get("imageUrl");
			
			Product addedProduct = adminProdService.addProductWithImage(name,description,price,stock,categoryId, imageURL);
			return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
			
		}
		catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went Wrong");
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteProduct(@RequestBody Map<String,Integer> requestBody) {
		try {
			Integer productId = requestBody.get("productId");
			adminProdService.deleteProduct(productId);
			return ResponseEntity.status(HttpStatus.OK).body("Product Deleted Successfully");
		}
		catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went Wrong");
		}
	}
	
	

}
