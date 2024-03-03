package org.thibault.cogiprestapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thibault.cogiprestapi.dto.CreateUserDTO;
import org.thibault.cogiprestapi.dto.UserDTO;
import org.thibault.cogiprestapi.services.UserService;
import org.thibault.cogiprestapi.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class UserController {
  
  private final UserService userService;
  
  public UserController(UserService userService){
    this.userService = userService;
  }
  
  @GetMapping ("/users")
  public List<UserDTO> getAllUsers(){
    List<UserDTO> userDTOs = new ArrayList<>();
    this.userService.getAllUsers().forEach(
            user -> {
              UserDTO mappedUser = mapUserToDTO(user);
              userDTOs.add(mappedUser);
    });
    return userDTOs;
  }
  
  @GetMapping ("/users/{id}")
  public UserDTO getUserById(@PathVariable("id") int id){
    return mapUserToDTO(this.userService.getUserById(id));
  }
  
  @PostMapping ("/users")
  public ResponseEntity<UserDTO> addUser(@RequestBody CreateUserDTO createUserDTO){
    User addedToRepositoryUser = this.userService.addUser(createUserDTO);
    UserDTO addedUserDTO = mapUserToDTO(addedToRepositoryUser);
    
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(addedUserDTO);
  }
  
  @PutMapping("/users/{id}")
  public ResponseEntity<UserDTO> updateUser(@PathVariable int id,
                              @RequestBody CreateUserDTO createUserDTO){
    User updatedUser = this.userService.updateUser(id, createUserDTO);
    UserDTO updatedUserDTO = mapUserToDTO(updatedUser);

    return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedUserDTO);
  }
  
  @DeleteMapping ("users/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable("id") int id){
    this.userService.deleteUser(id);
    
    return ResponseEntity
            .status(HttpStatus.OK)
            .body("User with id " + id + " has been deleted.");
  }
  
  
  private UserDTO mapUserToDTO(User user){
    return new UserDTO(user.getId(), user.getUsername(), user.getRole());
  }
  
  private User mapCreateUserDtoToUser(CreateUserDTO createUserDTO){
    return new User(createUserDTO.getUsername(), createUserDTO.getPassword(), createUserDTO.getRole());
  }
}