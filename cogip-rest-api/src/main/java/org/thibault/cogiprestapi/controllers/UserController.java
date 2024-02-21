package org.thibault.cogiprestapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<String> addUser(@RequestBody User user){
    this.userService.addUser(user);
    
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("User " + user.getUsername() + " added successfully.");
  }
  
  @PutMapping("/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable int id,
                              @RequestBody User user){
    User updatedUser = this.userService.updateUser(id, user);
    
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedUser);
  }
}