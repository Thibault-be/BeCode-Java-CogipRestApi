package org.thibault.cogiprestapi.controllers;

import org.thibault.cogiprestapi.repositories.UserRepository;
import org.thibault.cogiprestapi.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class UserController {
  
  private final UserRepository userRepository;
  
  public UserController(UserRepository userRepository){
    this.userRepository = userRepository;
  }
  
  
  @GetMapping ("/users")
  public List<User> getAllUsers(){
    return userRepository.getAllUsers();
  }
  
  
}