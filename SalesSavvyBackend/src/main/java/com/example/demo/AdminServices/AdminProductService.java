package com.example.demo.AdminServices;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Repositories.CategoryRepository;
import com.example.demo.Repositories.ProductImageRepository;
import com.example.demo.Repositories.ProductRepository;

@Service
public class AdminProductService {
	
	private final ProductRepository productRepo;
	private final ProductImageRepository productImageRepo;
	private final CategoryRepository categoryRepo;
	
	

	public AdminProductService(ProductRepository productRepo, ProductImageRepository productImageRepo,
			CategoryRepository categoryRepo) {
		super();
		this.productRepo = productRepo;
		this.productImageRepo = productImageRepo;
		this.categoryRepo = categoryRepo;
	}

	public Product addProductWithImage(String name, String description, Double price, Integer stock, Integer categoryId, String imageURL) {
		Optional<Category> category = categoryRepo.findById(categoryId);
		
		if(category.isEmpty()) {
			throw new IllegalArgumentException("Invalid Category ID");
		}
		
		Product product = new Product();
		product.setName(name);
		product.setDescription(description);
		product.setPrice(BigDecimal.valueOf(price));
        product.setStock(stock);
        product.setCategory(category.get());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        Product savedProduct = productRepo.save(product);
        
        if(imageURL!=null && !imageURL.isEmpty()) {
        	ProductImage prodImage = new ProductImage();
        	prodImage.setProduct(savedProduct);
        	prodImage.setImageUrl(imageURL);
        	productImageRepo.save(prodImage);
        }
        else {
        	throw new IllegalArgumentException("Product Image Url cannot be empty");
        }
        
        return savedProduct;
	}

	public void deleteProduct(Integer productId) {
		if(!productRepo.existsById(productId)) {
			throw new IllegalArgumentException("Product not found");
		}
		
		productImageRepo.deleteByProductId(productId);
		
		productRepo.deleteById(productId);
		
	}

}
