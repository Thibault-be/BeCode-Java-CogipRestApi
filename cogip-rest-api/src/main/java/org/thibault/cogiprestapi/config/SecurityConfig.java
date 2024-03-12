package org.thibault.cogiprestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((authorizeObj) -> {
              authorizeObj.anyRequest().authenticated();
            }).httpBasic(Customizer.withDefaults());
    return httpSecurity.build();
  }
  
  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService(){
    UserDetails admin = User.builder()
            .username("admin")
            //.password(passwordEncoder().encode("admin"))
            .password("admin")
            .roles("ADMIN")
            .build();

    UserDetails accountant = User.builder()
            .username("accountant")
            //.password(passwordEncoder().encode("accountant"))
            .password("accountant")
            .roles("ACCOUNTANT")
            .build();

    UserDetails intern = User.builder()
            .username("intern")
            //.password(passwordEncoder().encode("intern"))
            .password("intern")
            .roles("INTERN")
            .build();

    return new InMemoryUserDetailsManager(admin, accountant, intern);
  }
}
