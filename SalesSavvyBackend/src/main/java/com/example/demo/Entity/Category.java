package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="categories")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int category_id;
	
	
	@Column(name = "category_name", nullable = false,unique = true)
	private String categoryName;


	public Category() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Category(int category_id, String categoryName) {
		super();
		this.category_id = category_id;
		this.categoryName = categoryName;
	}


	public Category(String categoryName) {
		super();
		this.categoryName = categoryName;
	}


	public int getCategory_id() {
		return category_id;
	}


	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
    
}
