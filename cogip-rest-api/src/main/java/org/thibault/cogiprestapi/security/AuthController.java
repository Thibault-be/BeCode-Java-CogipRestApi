package org.thibault.cogiprestapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thibault.cogiprestapi.dto.CreateUserDTO;
import org.thibault.cogiprestapi.repositories.UserRepository;

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
    
    CreateUserDTO userEntity = new CreateUserDTO();
    userEntity.setUsername(userCredentials.getUsername());
    userEntity.setPassword(this.passwordEncoder.encode(userCredentials.getPassword()));
    userEntity.setRole(userCredentials.getRole());
    
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
  }
}