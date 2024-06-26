package org.thibault.cogiprestapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  
  private JwtAuthEntryPoint jwtAuthEntryPoint;
  private CustomUserDetailsService userDetailsService;
  
  public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthEntryPoint jwtAuthEntryPoint) {
    this.userDetailsService = userDetailsService;
    this.jwtAuthEntryPoint = jwtAuthEntryPoint;
  }
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(this.jwtAuthEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());
    
      http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
      return http.build();
  }
  
  @Bean
  public JWTAuthenticationFilter jwtAuthenticationFilter(){
    return new JWTAuthenticationFilter();
  }
  
  @Bean
  public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception{
    return authenticationConfiguration.getAuthenticationManager();
  }
  
  @Bean
  PasswordEncoder passwordEncoder(){
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    return passwordEncoder;
  }
}
