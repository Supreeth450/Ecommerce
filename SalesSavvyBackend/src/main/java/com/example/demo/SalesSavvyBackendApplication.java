package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.demo.Controller.*;
import com.example.demo.Entity.*;
import com.example.demo.Services.*;
import com.example.demo.Repositories.*;

@SpringBootApplication
public class SalesSavvyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesSavvyBackendApplication.class, args);
	}

}
