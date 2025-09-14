package com.example.demo.Filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.users;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.AuthService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = {"/api/","/admin/"})
@Component
public class AuthenticationFilter implements Filter{
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
	private final AuthService authService;
	private final UserRepository userRepo;
	
	private static final String ALLOWED_ORIGIN = "http://localhost:5173";
	
	private static final String[] UNAUTHENTICATED_PATHS = { 
			"/api/users/register",
			"/api/auth/login"
	};
	

	public AuthenticationFilter(AuthService authService, UserRepository userRepo) {
		System.out.println("Filter Started.");
		this.authService = authService;
		this.userRepo = userRepo;
	}



	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		try {
			executeFilterLogic(request,response,chain);
		}
		catch (Exception e) {
			logger.error("Unexpected error in AuthenticationFilter",e);
			sendErrorResponse((HttpServletResponse) response,
			HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal Server Error");
		}	
		
	}

	
     private void executeFilterLogic(ServletRequest request, ServletResponse response, FilterChain chain)
    		 throws IOException, ServletException{
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String requestURI = httpRequest.getRequestURI();
		logger.info("{Request URI: {}}",requestURI);
		
		if(Arrays.asList(UNAUTHENTICATED_PATHS).contains(requestURI)) {
			chain.doFilter(request, response);
			return;
		}
		
		if(httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
			setCORSHeaders(httpResponse);
			return;
		}
		
		String token = getAuthTokenFromCookies(httpRequest);
		System.out.println(token);
		
		if(token == null || !authService.validateToken(token)) {
			sendErrorResponse(httpResponse,HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized: Invalid or missing token");
			return;
		}
		
		String Email = authService.extractUserEmail(token);
		Optional<users> userOptional = userRepo.findByEmail(Email);
		if(userOptional.isEmpty()) {
			sendErrorResponse(httpResponse,HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized: User not found");
			return;
		}
		
		
		users authenticatedUser = userOptional.get();
		Role role = authenticatedUser.getRole();
		logger.info("Authenticated User: {}, Role: {}", authenticatedUser.getUserName(),role);
		
		
		if(requestURI.startsWith("/admin/") && role!=Role.ADMIN) {
			sendErrorResponse(httpResponse,HttpServletResponse.SC_FORBIDDEN,"Forbidden: Admin access required");
			return;
		}
		
		if(requestURI.startsWith("/api/") && role!=Role.CUSTOMER) {
			sendErrorResponse(httpResponse,HttpServletResponse.SC_FORBIDDEN,"Forbidden: Customer access required");
			return;
		}
		
		httpRequest.setAttribute("authenticatedUser", authenticatedUser);
		chain.doFilter(request, response);
	}


	private String getAuthTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		
		if(cookies!=null) {
			return Arrays.stream(cookies)
					.filter(cookie -> "authToken".equals(cookie.getName()))
					.map(Cookie::getValue)
					.findFirst()
					.orElse(null);
		}
		return null;
	}



	private void setCORSHeaders(HttpServletResponse response) {
		response.setHeader ("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
		response.setHeader ("Access-Control-Allow-Headers", "Content-Type,Authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setStatus(HttpServletResponse.SC_OK);
		
	}



	private void sendErrorResponse(HttpServletResponse response, int scInternalServerError, String string) throws IOException{
	response.setStatus(scInternalServerError);
	response.getWriter().write(string);	
	}



	

}
