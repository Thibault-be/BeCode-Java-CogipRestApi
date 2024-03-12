package org.thibault.cogiprestapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thibault.cogiprestapi.dto.CreateUserDTO;
import org.thibault.cogiprestapi.model.User;
import org.thibault.cogiprestapi.repositories.UserRepository;

import java.util.Optional;

@RestController
public class AuthController {
  
  private AuthenticationManager authenticationManager;
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private JWTGenerator jwtGenerator;
  
  @Autowired
  public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder,
                        JWTGenerator jwtGenerator) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtGenerator = jwtGenerator;
  }
  
  @PostMapping ("/register")
  public ResponseEntity<String> register(@RequestBody UserCredentials userCredentials){
    
    System.out.println("in authcontoller register");
    System.out.println("credentials username: " + userCredentials.getUsername());
    System.out.println("credentials password: " + userCredentials.getPassword());
    System.out.println("credentials role: " + userCredentials.getRole().name());
    
    CreateUserDTO userEntity = new CreateUserDTO();
    userEntity.setUsername(userCredentials.getUsername());
    userEntity.setPassword(this.passwordEncoder.encode(userCredentials.getPassword()));
    userEntity.setRole(userCredentials.getRole());
    
    System.out.println("entity username: " + userEntity.getUsername());
    System.out.println("entity password: " + userEntity.getPassword());
    System.out.println("entity role: " + userEntity.getRole().name());
    
    this.userRepository.addUser(userEntity);
    
    return new ResponseEntity<>("Registered successfully", HttpStatus.OK);
  }
  
  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@RequestBody UserCredentials credentials) {
    
    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
    
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = this.jwtGenerator.generateToken(authentication);
    return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    
//    // Retrieve user from the database based on the provided username
//    Optional<User> optionalUser = Optional.ofNullable(userRepository.getUsersByFilters(null, credentials.getUsername(), null).get(0));
//
//    if (optionalUser.isPresent()) {
//      User user = optionalUser.get();
//      // Check if the provided password matches the password stored in the database
//      if (user.getPassword().equals(credentials.getPassword())) {
//        return "Login successful";
//      } else {
//        return "Invalid password";
//      }
//    } else {
//      return "User not found";
//    }
  }
}