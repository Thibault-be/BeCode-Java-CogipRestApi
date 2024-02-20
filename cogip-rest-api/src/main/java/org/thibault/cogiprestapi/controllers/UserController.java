package org.thibault.cogiprestapi.controllers;

import org.springframework.web.bind.annotation.*;
import org.thibault.cogiprestapi.services.UserService;
import org.thibault.cogiprestapi.model.User;

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
  
  @GetMapping ("/users/{id}")
  public User getUserById(@PathVariable String id){
    return this.userService.getUserById(id);
  }
  
  @PostMapping ("/users")
  public void addUser(@RequestBody User user){
    System.out.println("I AM HERE");
    System.out.println(user.getUsername());
    this.userService.addUser(user);
  }
}