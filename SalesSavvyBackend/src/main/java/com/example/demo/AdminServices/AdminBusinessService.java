package com.example.demo.AdminServices;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Order;
import com.example.demo.Entity.OrderItem;
import com.example.demo.Entity.OrderStatus;
import com.example.demo.Repositories.OrderItemRepository;
import com.example.demo.Repositories.OrderRepository;
import com.example.demo.Repositories.ProductRepository;

@Service
public class AdminBusinessService {
	
	private final OrderRepository orderRepo;
	private final OrderItemRepository orderItemRepo;
	private final ProductRepository productRepo;
	

	public AdminBusinessService(OrderRepository orderRepo, OrderItemRepository orderItemRepo,
			ProductRepository productRepo) {
		super();
		this.orderRepo = orderRepo;
		this.orderItemRepo = orderItemRepo;
		this.productRepo = productRepo;
	}

	public Map<String, Object> calculateMonthlyBusiness(int month, int year) {
		List<Order> successfulOrders = orderRepo.findSuccessfulOrdersByMonthAndYear(month, year);
		return calculateBusinessMetrices(successfulOrders);
	}


	public Map<String, Object> calculateYearlyBusiness(int year) {
		List<Order> successfulOrders = orderRepo.findSuccessfulOrdersByYear(year);
		return calculateBusinessMetrices(successfulOrders);
	}

	public Map<String, Object> calculateDailyBusiness(LocalDate localdate) {
		List<Order> successfulOrders = orderRepo.findSuccessfulOrdersByDate(localdate);
		return calculateBusinessMetrices(successfulOrders);
	}

	public Map<String, Object> calculateOverallBusiness() {
		List<Order> successfulOrders = orderRepo.findAllByStatus(OrderStatus.SUCCESS);
		BigDecimal totalBusiness = orderRepo.calculateOverAllBusiness();
		Map<String, Object> response = calculateBusinessMetrices(successfulOrders);
		response.put("totalBusiness", totalBusiness.doubleValue());
		return response;
	}
	
	private Map<String, Object> calculateBusinessMetrices(List<Order> orders) {
		double totalRevenue = 0.0;
		
		Map<String,Integer> categorySales = new HashMap<>();
		
		for(Order order:orders) {
			totalRevenue += order.getTotalAmount().doubleValue();
			
			List<OrderItem> items = orderItemRepo.findByOrderId(order.getOrderId());
			
			for(OrderItem item:items) {
				String categoryName = productRepo.findCategoryNameByProductId(item.getProductId());
				categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
			}
		}
		
		Map<String,Object> metrices = new HashMap<>();
		metrices.put("totalBusiness", totalRevenue);
		metrices.put("categorySales", categorySales);
		return metrices;
	}

}
