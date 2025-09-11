package com.example.demo.Services;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.JWTToken;
import com.example.demo.Entity.users;
import com.example.demo.Repositories.JWTTokenRepository;
import com.example.demo.Repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;




@Service
public class AuthService {
	private final Key SIGNING_KEY;
	
	private final UserRepository userRepo;
	
	private final JWTTokenRepository tokenRepo;
	
	private final BCryptPasswordEncoder passwordEncoder;
	
	private final String secret;

	public AuthService(UserRepository userRepo, JWTTokenRepository tokenRepo, @Value("${jwt.secret}") String jwtSecret) {
		super();
	
		this.userRepo = userRepo;
		this.tokenRepo = tokenRepo;
		this.passwordEncoder = new BCryptPasswordEncoder();
		
		this.secret = jwtSecret;
		
		if(jwtSecret.getBytes(StandardCharsets.UTF_8).length < 64) {
			throw new IllegalArgumentException("JWT_SECRET in application.properties must be at least 64 bytes long for HS512.");
		}
		
		this.SIGNING_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
	
	
	public users authenticate(String email,String password) {
		users user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid Email or password"));
		
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Invalid Email or password");
		}
		
		return user;
		
	}
	
	
	public String generateToken(users user) {
		String token;
		LocalDateTime now = LocalDateTime.now();
		
		JWTToken existingToken =  tokenRepo.findByuserId(user.getUser_id());
	    
		if(existingToken != null && now.isBefore(existingToken.getExpiresAt())) {
			token = existingToken.getToken();
		}
		else {
			token  = generateNewToken(user);
			if(existingToken!=null) {
				tokenRepo.delete(existingToken);
			}
			saveToken(user,token);
			
		}
    return token;
}


	private void saveToken(users user, String token) {
		  JWTToken jwtToken = new JWTToken(user,token,LocalDateTime.now().plusHours(1));
		  tokenRepo.save(jwtToken);
		
	}


	public String generateNewToken(users user) {
		return Jwts.builder()
				.setSubject(user.getEmail())
				.claim("role", user.getRole())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+3600000))
				.signWith(SIGNING_KEY,SignatureAlgorithm.HS512)
				.compact();
	}


	public boolean validateToken(String token) {
	    try {
	        System.err.println("VALIDATING TOKEN...");

	        Jwts.parserBuilder()
	            .setSigningKey(SIGNING_KEY)
	            .build()
	            .parseClaimsJws(token);

	        Optional<JWTToken> jwtToken = tokenRepo.findByToken(token);
	        if (jwtToken.isPresent()) {
	            System.err.println("Token Expiry: " + jwtToken.get().getExpiresAt());
	            System.err.println("Current Time: " + LocalDateTime.now());
	            return jwtToken.get().getExpiresAt().isAfter(LocalDateTime.now());
	        }

	        return false;
	    } catch (Exception e) {
	        System.err.println("Token validation failed: " + e.getMessage());
	        return false;
	    }
	}



	public String extractUserEmail(String token) {
	    return Jwts.parserBuilder()
	        .setSigningKey(secret.getBytes())
	        .build()
	        .parseClaimsJws(token)
	        .getBody()
	        .getSubject();
    
	}
	
	
	
	

	
}
