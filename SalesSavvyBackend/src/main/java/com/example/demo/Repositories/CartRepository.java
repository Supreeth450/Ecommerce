package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.CartItem;


@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer>{

	@Query("SELECT c FROM CartItem c where c.user.user_id=:userId AND c.product.productId=:productId")
	Optional<CartItem> findByusersAndProduct(@Param("userId") int userId, @Param("productId") int productId);

	@Query("SELECT COUNT(c) FROM CartItem c WHERE c.user.user_id = :userId")
	int countByUserId(@Param("userId") int userId);

}
