package com.example.demo.Entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="order_items")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int id;
	 
	
	 private Order order;
	 
	 @Column(name="product_id",nullable = false)
	 private int productId;
	 
	 @Column(name="quantity",nullable = false)
	 private int quantity;
	 
	 @Column(name = "price_per_unit",nullable = false)
	 private BigDecimal pricePerCount;
	 
	 @Column(name = "total_price",nullable = false)
	 private BigDecimal totalPrice;

	 public OrderItem() {
		super();
		// TODO Auto-generated constructor stub
	 }

	 public OrderItem(Order order, int productId, int quantity, BigDecimal pricePerCount, BigDecimal totalPrice) {
		super();
		this.order = order;
		this.productId = productId;
		this.quantity = quantity;
		this.pricePerCount = pricePerCount;
		this.totalPrice = totalPrice;
	 }

	 public OrderItem(int id, Order order, int productId, int quantity, BigDecimal pricePerCount,
			BigDecimal totalPrice) {
		super();
		this.id = id;
		this.order = order;
		this.productId = productId;
		this.quantity = quantity;
		this.pricePerCount = pricePerCount;
		this.totalPrice = totalPrice;
	 }

	 public int getId() {
		 return id;
	 }

	 public void setId(int id) {
		 this.id = id;
	 }

	 public Order getOrder() {
		 return order;
	 }

	 public void setOrder(Order order) {
		 this.order = order;
	 }

	 public int getProductId() {
		 return productId;
	 }

	 public void setProductId(int productId) {
		 this.productId = productId;
	 }

	 public int getQuantity() {
		 return quantity;
	 }

	 public void setQuantity(int quantity) {
		 this.quantity = quantity;
	 }

	 public BigDecimal getPricePerCount() {
		 return pricePerCount;
	 }

	 public void setPricePerCount(BigDecimal pricePerCount) {
		 this.pricePerCount = pricePerCount;
	 }

	 public BigDecimal getTotalPrice() {
		 return totalPrice;
	 }

	 public void setTotalPrice(BigDecimal totalPrice) {
		 this.totalPrice = totalPrice;
	 }
	 
	 
}
