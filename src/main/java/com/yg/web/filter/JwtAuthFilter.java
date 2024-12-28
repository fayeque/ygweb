package com.yg.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yg.web.config.UserInfoUserDetailsService;
import com.yg.web.utils.JwtService;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoUserDetailsService userDetailsService;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        System.out.println("inside jwt filter");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
            username = jwtService.extractUsername(token);
            Map<String, String> resultMap = objectMapper.readValue(username, new TypeReference<Map<String, String>>() {});
            username = resultMap.get("username"); 
            System.out.println("username in jwt " + username);
            }catch(Exception e) {
            	System.out.println("Token is expired request in jwtAuthFilter 1st exception block");
//            	 throw new JwtTokenExpiredException();
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				try {
					if (jwtService.validateToken(token, userDetails)) {
						System.out.println("before UsernamePasswordAuthenticationToken");
					    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					    System.out.println("after UsernamePasswordAuthenticationToken");
					    System.out.println(" before authToken.setDetails");
					    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                System.out.println(" after authToken.setDetails" + authToken.getPrincipal());
					    SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

        }
        filterChain.doFilter(request, response);
    }
}
