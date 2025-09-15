package com.example.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.CartItem;

import jakarta.transaction.Transactional;


@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer>{

	@Query("SELECT c FROM CartItem c where c.user.user_id=:userId AND c.product.productId=:productId")
	Optional<CartItem> findByusersAndProduct(@Param("userId") int userId, @Param("productId") int productId);

	@Query("SELECT COALESCE(sum(c.quantity),0) From CartItem c WHERE c.user.user_id = :userId")
	int getCartItemCount(@Param("userId") int userId);

	
	@Query("SELECT c From CartItem c JOIN FETCH c.product p LEFT JOIN FETCH ProductImage pi ON p.productId = pi.product.productId WHERE c.user.user_id = :userId")
	List<CartItem> findCartItemsWithProductDetails(@Param("userId") int userId);
	
	
	@Query("UPDATE CartItem c SET c.quantity = :quantity WHERE c.id = : cartItemId")
	void updateCartItemQuantity(int cartItemId,int quantity);

	
	@Modifying
	@Transactional
	@Query("DELETE FROM CartItem c WHERE c.user.user_id = :user_id AND c.product.productId = :product_id")
	void deleteCartItem(int user_id, int product_id);
	
	
	@Modifying
	@Transactional
	@Query("DELETE FROM CartItem c WHERE c.user.user_id = :user_id")
	void deleteAllCartItemsByUserId(int user_id);
	
	

}
