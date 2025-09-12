package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.CartItem;


@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer>{

}
