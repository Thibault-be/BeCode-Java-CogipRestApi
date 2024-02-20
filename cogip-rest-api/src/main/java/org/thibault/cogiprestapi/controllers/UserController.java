package org.thibault.cogiprestapi.controllers;

import org.thibault.cogiprestapi.services.UserService;
import org.thibault.cogiprestapi.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class UserController {
  
  private final UserService userService;
  
  public UserController(UserService userService){
    this.userService = userService;
  }
  
  
  @GetMapping ("/users")
  public List<User> getAllUsers(){
    return this.userService.getAllUsers();
  }
  
  
}