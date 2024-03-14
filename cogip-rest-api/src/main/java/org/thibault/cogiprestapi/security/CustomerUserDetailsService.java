package org.thibault.cogiprestapi.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thibault.cogiprestapi.enums.UserRole;
import org.thibault.cogiprestapi.model.User;
import org.thibault.cogiprestapi.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerUserDetailsService implements UserDetailsService {
  
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  
  public CustomerUserDetailsService(UserRepository userRepository ) {
    this.userRepository = userRepository;
  }
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    System.out.println("Username in load by username: " + user.getUsername());
    System.out.println("Password in loadbyusername: " + user.getPassword());
    List<GrantedAuthority> list = mapRoleToAuthorities(user.getRole());
    System.out.println("Granted Authorities: " + list);
    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRoleToAuthorities(user.getRole()) );
  }
  
  private List<GrantedAuthority> mapRoleToAuthorities(UserRole role){
    List<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();
    GrantedAuthority granted = new SimpleGrantedAuthority("ROLE_"+role.name());
    grantedAuthoritiesList.add(granted);
    return grantedAuthoritiesList;
  }
}
