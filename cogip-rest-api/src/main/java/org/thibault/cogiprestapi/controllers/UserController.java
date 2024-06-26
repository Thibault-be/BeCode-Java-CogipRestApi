package org.thibault.cogiprestapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thibault.cogiprestapi.dto.CreateUserDTO;
import org.thibault.cogiprestapi.dto.UserDTO;
import org.thibault.cogiprestapi.enums.UserRole;
import org.thibault.cogiprestapi.exceptions.IllegalParametersException;
import org.thibault.cogiprestapi.model.User;
import org.thibault.cogiprestapi.services.UserService;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping
public class UserController {
  
  private final UserService userService;
  
  public UserController(UserService userService){
    this.userService = userService;
  }
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ACCOUNTANT')")
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
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ACCOUNTANT')")
  @GetMapping ("/users/{id}")
  public UserDTO getUserById(@PathVariable("id") int id){
    return mapUserToDTO(this.userService.getUserById(id));
  }
  
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ACCOUNTANT')")
  @GetMapping ("/users/search")
  public List<UserDTO> getUsersByFilters(@RequestParam (required = false) Integer id,
                                         @RequestParam (required = false) String username,
                                         @RequestParam (required = false) String role){
    UserRole userRole = convertStringToRole(role);
    List<UserDTO> userDTOs = new ArrayList<>();
    this.userService.getUsersByFilters(id, username, userRole).stream()
            .forEach(user -> {
              UserDTO mappedUser = mapUserToDTO(user);
              userDTOs.add(mappedUser);
            });
    return userDTOs;
  }
  
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping ("/users")
  public ResponseEntity<UserDTO> addUser(@RequestBody CreateUserDTO createUserDTO){
    User addedToRepositoryUser = this.userService.addUser(createUserDTO);
    UserDTO addedUserDTO = mapUserToDTO(addedToRepositoryUser);
    
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(addedUserDTO);
  }
  
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("/users/{id}")
  public ResponseEntity<UserDTO> updateUser(@PathVariable int id,
                              @RequestBody CreateUserDTO createUserDTO){
    User updatedUser = this.userService.updateUser(id, createUserDTO);
    UserDTO updatedUserDTO = mapUserToDTO(updatedUser);

    return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedUserDTO);
  }
  
  @PreAuthorize("hasRole('ROLE_ADMIN')")
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
  
  private UserRole convertStringToRole(String role){
    
    if (role == null) return null;
    
    if (role.equalsIgnoreCase("admin")){
      return UserRole.ADMIN;
    }
    if (role.equalsIgnoreCase("accountant")){
      return UserRole.ACCOUNTANT;
    }
    if (role.equalsIgnoreCase("intern")){
      return UserRole.INTERN;
    }
    throw new IllegalParametersException("Please enter a valid role. Options: admin, accountant or intern.");
  }
}