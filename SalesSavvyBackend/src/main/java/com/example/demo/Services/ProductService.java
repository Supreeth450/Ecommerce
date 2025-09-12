package com.example.demo.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.Product;
import com.example.demo.Entity.ProductImage;
import com.example.demo.Repositories.CategoryRepository;
import com.example.demo.Repositories.ProductImageRepository;
import com.example.demo.Repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository prodRepo;
	
	@Autowired
	private ProductImageRepository prodImageRepo;
	
	@Autowired
	private CategoryRepository catRepo;

	public List<Product> getProductsByCategory(String categoryName) {
		if(categoryName!=null && !categoryName.isEmpty()) {
			Optional<Category> categoryOpt = catRepo.findByCategoryName(categoryName);
			
			if(categoryOpt.isPresent()) {
				Category category = categoryOpt.get();
				return prodRepo.findByCategory_CategoryId(category.getCategoryId());
			} else {
				throw new RuntimeException("Category not found");
			}
			
		} else {
			return prodRepo.findAll();
		}
	}

	public List<String> getProductImages(int product_id) {
		List<ProductImage> productImages = prodImageRepo.findByProduct_ProductId(product_id);
		
		List<String> imageUrls = new ArrayList<>();
		
		for(ProductImage image : productImages) {
			imageUrls.add(image.getImageUrl());
		}
		return imageUrls;
	}

}
