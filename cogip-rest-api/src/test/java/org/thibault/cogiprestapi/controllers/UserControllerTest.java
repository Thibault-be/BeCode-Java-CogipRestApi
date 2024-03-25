package org.thibault.cogiprestapi.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.thibault.cogiprestapi.dto.CreateUserDTO;
import org.thibault.cogiprestapi.dto.UserDTO;
import org.thibault.cogiprestapi.enums.UserRole;
import org.thibault.cogiprestapi.model.User;
import org.thibault.cogiprestapi.services.UserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserControllerTest {
  
  
  @Test
  @DisplayName("Verifies if userService.getAllUsers is called")
  void getAllUsersHappyFlow(){
    UserService userService = mock(UserService.class);
    UserController userController= new UserController(userService);
    
    User admin = new User("admin", "admin", UserRole.ADMIN);
    User accountant = new User("accountant", "accountant", UserRole.ACCOUNTANT);
    User intern = new User("intern", "intern", UserRole.INTERN);
    List<User> users = Arrays.asList(admin, accountant, intern);
    given(userService.getAllUsers()).willReturn(users);
    List<UserDTO> result = userController.getAllUsers();
    verify(userService).getAllUsers();
  }
  
  @Test
  @DisplayName("Verifies if userService.getUsersByFilters is called")
  void getFilteredUsersHappyFlow(){
    UserService userService = mock(UserService.class);
    UserController userController= new UserController(userService);
    
    User admin = new User("admin", "admin", UserRole.ADMIN);
    User accountant = new User("accountant", "accountant", UserRole.ACCOUNTANT);
    User intern = new User("intern", "intern", UserRole.INTERN);
    List<User> users = Arrays.asList(admin, accountant, intern);
    
    given(userService.getUsersByFilters(null, null, UserRole.INTERN))
            .willReturn(users);
    List<UserDTO> result = userController.getUsersByFilters(null, null, "intern");
    verify(userService).getUsersByFilters(null, null, UserRole.INTERN);
  }
  
  @Test
  @DisplayName("Verifies if userService.addUser is called")
  void addUserHappyFlow(){
    UserService userService = mock(UserService.class);
    UserController userController= new UserController(userService);
    CreateUserDTO userDTO = new CreateUserDTO("thibault", "thibault", UserRole.ADMIN);
    User user = new User("thibault", "thibault", UserRole.ADMIN);
    
    given(userService.addUser(userDTO)).willReturn(user);
    ResponseEntity<UserDTO> result = userController.addUser(userDTO);
    
    verify(userService).addUser(userDTO);
  }
  
  @Test
  @DisplayName("Verifies if userService.addUser is still called when parameters are missing in CreateUserDTO")
  void addUserParameterMissingHappyFlow(){
    UserService userService = mock(UserService.class);
    UserController userController= new UserController(userService);
    CreateUserDTO userDTO = new CreateUserDTO("thibault", null, UserRole.ADMIN);
    User user = new User("thibault", "thibault", UserRole.ADMIN);
    
    given(userService.addUser(userDTO)).willReturn(user);
    ResponseEntity<UserDTO> result = userController.addUser(userDTO);
    verify(userService).addUser(userDTO);
  }
  
  @Test
  @DisplayName("Verifies if userService.update user is called.")
  void updateUserHappyFlow(){
    int id = 5;
    UserService userService = mock(UserService.class);
    UserController userController= new UserController(userService);
    CreateUserDTO userDTO = new CreateUserDTO("thibault", null, UserRole.ADMIN);
    User user = new User("thibault", "thibault", UserRole.ADMIN);
    
    given(userService.updateUser(id, userDTO)).willReturn(user);
    ResponseEntity<UserDTO> result = userController.updateUser(id, userDTO);
    verify(userService).updateUser(id, userDTO);
  }
  
  @Test
  @DisplayName("Verifies if userService.delete user is called.")
  void deleteUserHappyFlow(){
    int id = 5;
    UserService userService = mock(UserService.class);
    UserController userController = new UserController(userService);
    
    ResponseEntity<String> result = userController.deleteUser(id);
    verify(userService).deleteUser(id);
    
  }
  
}