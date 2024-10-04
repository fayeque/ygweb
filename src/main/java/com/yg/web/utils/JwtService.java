package com.yg.web.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {


    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    
    private ObjectMapper objectMapper = new ObjectMapper();


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
    	
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) throws Exception {
        String username = extractUsername(token);
        Map<String, String> resultMap = objectMapper.readValue(username, new TypeReference<Map<String, String>>() {});
        username = resultMap.get("username"); 
        System.out.println("username " + username);
        return (username).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,userDetails);
    }

    private String createToken(Map<String, Object> claims, UserDetails userDetails) {
    	 UserJwtDetails userJwtDetails = new UserJwtDetails(userDetails.getUsername());
    	 ObjectMapper objectMapper = new ObjectMapper();
    	 String userJwtDetailJson;
         try {
        	  userJwtDetailJson =objectMapper.writeValueAsString(userJwtDetails);
         } catch (JsonProcessingException e) {
             throw new RuntimeException("Error converting UserJwtDetail to JSON", e);
         }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userJwtDetailJson)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*90))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
    
    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
