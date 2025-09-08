package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.users;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<users, Integer> {
    Optional<users> findByEmail(String email); 
    Optional<users> findByuserName(String userame); 
}
