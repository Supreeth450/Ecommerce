package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.ProductImage;

import jakarta.transaction.Transactional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer>{
        
	@Query("SELECT pi FROM ProductImage pi WHERE pi.product.productId = :productId")
	List<ProductImage>findByProduct_ProductId(@Param("productId") int productId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM ProductImage pi WHERE pi.product.productId = :productId")
	void deleteByProductId(@Param("productId") int productId);
}
