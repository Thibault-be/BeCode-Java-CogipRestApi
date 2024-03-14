package org.thibault.cogiprestapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
  
  @Autowired
  private JWTGenerator tokenGenerator;
  
  @Autowired
  private CustomerUserDetailsService customerUserDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    String token = getJWTfromRequest(request);
    
    if (StringUtils.hasText(token) && this.tokenGenerator.validateToken(token)){
      String username = tokenGenerator.getUsernameFromJWT(token);
      System.out.println("In JWTAuthenticationFilter about to load UserDetails by calling customerUserDetailsService.loadUserByUsername");
      UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
      System.out.println("Back in JWTAuthenticationFilter. UserDetails was successful");
      System.out.println("Printing the authenticationToken:\n" + authenticationToken);
      
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    
    filterChain.doFilter(request, response);
    
  }
  
  private String getJWTfromRequest(HttpServletRequest request){
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(("Bearer "))){
      return bearerToken.substring(7, bearerToken.length());
    }
    return null;
  }
}
