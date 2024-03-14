package org.thibault.cogiprestapi.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTGenerator {
  
  public String generateToken(Authentication authentication){
    System.out.println("generating the token");
    String username = authentication.getName();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
    
    String token = Jwts.builder()
            .setSubject(username)
            .issuedAt(currentDate)
            .expiration(expireDate)
            .signWith(SignatureAlgorithm.HS256, SecurityConstants.JWT_SECRET)
            .compact();
    System.out.println("printing the token in JWTGenerator generate token\n" + token);
    
    return token;
  }
  
  public String getUsernameFromJWT(String token){
    Claims claims= Jwts.parser()
            .setSigningKey(SecurityConstants.JWT_SECRET)
            .build()
            .parseSignedClaims(token)
            .getPayload();
            
    return claims.getSubject();
  }
  
  public boolean validateToken(String token){
    try{
      System.out.println("First line in validate token and printing it:");
      System.out.println(token);
      Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).build().parseSignedClaims(token);
      System.out.println("After parser in validate token. About to return true.");
      return true;
    } catch (Exception e) {
      System.out.println("Validate token failed: " + e.getMessage());
      throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
      
    }
  }
  
}
