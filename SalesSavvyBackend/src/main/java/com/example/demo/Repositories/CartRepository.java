package com.example.demo.Repositories;

import java.util.List;
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

	@Query("SELECT COALESCE(sum(c.quantity),0) From CartItem c WHERE c.user.user_id = :userId")
	int getCartItemCount(@Param("userId") int userId);

	
	@Query("SELECT c From CartItem c JOIN FETCH c.product p LEFT JOIN FETCH ProductImage pi ON p.productId = pi.product.productId WHERE c.user.user_id = :userId")
	List<CartItem> findCartItemsWithProductDetails(@Param("userId") int userId);

}
