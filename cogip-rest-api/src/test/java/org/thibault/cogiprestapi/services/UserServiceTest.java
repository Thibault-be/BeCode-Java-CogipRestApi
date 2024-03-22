package org.thibault.cogiprestapi.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thibault.cogiprestapi.dto.CreateUserDTO;
import org.thibault.cogiprestapi.enums.UserRole;
import org.thibault.cogiprestapi.exceptions.DuplicateValueException;
import org.thibault.cogiprestapi.exceptions.IdNotFoundException;
import org.thibault.cogiprestapi.exceptions.ParametersMissingException;
import org.thibault.cogiprestapi.exceptions.ResultSetEmptyException;
import org.thibault.cogiprestapi.model.User;
import org.thibault.cogiprestapi.repositories.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

public class UserServiceTest {
  
  @Test
  @DisplayName("Tests if userRepository.getAllUsers is called")
  void getAllUsersHappyFlow(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    
    User admin = new User("admin", "admin", UserRole.ADMIN);
    User accountant = new User("accountant", "accountant", UserRole.ACCOUNTANT);
    User intern = new User("intern", "intern", UserRole.INTERN);
    List<User> users = Arrays.asList(admin, accountant, intern);
    given(userRepository.getAllUsers()).willReturn(users);
    List<User> result = userService.getAllUsers();
    verify(userRepository).getAllUsers();
  }
  
  @Test
  @DisplayName("If no users are found, a ResultSetEmptyException should be thrown")
  void getAllUsersEmptyResultSetException(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    given(userRepository.getAllUsers()).willReturn(Collections.emptyList());
    assertThrows(ResultSetEmptyException.class, userService::getAllUsers);
    verify(userRepository).getAllUsers();
  }
  
  @Test
  @DisplayName("Verifies if userRepository.getUserById is called")
  void getUserByIdHappyFlow(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    int id = 5;
    User user = new User("thibault", "thibault", UserRole.ADMIN);
    given(userRepository.getUserById(id)).willReturn(user);
    User result = userService.getUserById(id);
    verify(userRepository).getUserById(id);
  }
  
  @Test
  @DisplayName("Verifies if userRepository.getUserById throws IdNotFoundException when looking for non-existent id")
  void getUserByIdThrowsIdNotFoundException(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    int id = 5;
    given(userRepository.getUserById(id)).willThrow(EmptyResultDataAccessException.class);
    
    assertThrows(IdNotFoundException.class, () -> {
      userService.getUserById(id);
    });
  }
  
  @Test
  @DisplayName("getUsersByFilters calls userRepository.getUsersByFilters")
  void getUsersByFiltersHappyFlow(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    
    User accountant = new User("accountant", "accountant", UserRole.ACCOUNTANT);
    User thibault = new User("thibault", "thibault", UserRole.INTERN);
    List<User> users = Arrays.asList(accountant, thibault);
    
    given (userRepository.getUsersByFilters(null, null, UserRole.ACCOUNTANT))
            .willReturn(users);
    
    List<User> result = userService.getUsersByFilters(null, null, UserRole.ACCOUNTANT);
    
    verify(userRepository).getUsersByFilters(null, null, UserRole.ACCOUNTANT);
  }
  
  @Test
  @DisplayName("Empty resultset from getUsersByFilters throws ResultSetEmptyException")
  void getUsersByFilterEmptyResultSetException(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    
    given(userRepository.getUsersByFilters(5, "thibault", UserRole.INTERN))
            .willReturn(Collections.emptyList());
    
    assertThrows(ResultSetEmptyException.class, () ->{
      userService.getUsersByFilters(5, "thibault", UserRole.INTERN);
    });
  }
  
  @Test
  @DisplayName("Verifies that userRepository is called.")
  void addUserHappyFlow(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    
    CreateUserDTO newUser = new CreateUserDTO("thibault", "thibault", UserRole.ADMIN);
    User user = new User("thibault", "thibault", UserRole.ADMIN);
    
    given(userRepository.addUser(newUser)).willReturn(user);
    
    User result = userService.addUser(newUser);
    verify(userRepository).addUser(newUser);
  }
  
  @Test
  @DisplayName("When username already exists, DuplicateValueException is thrown.")
  void addUserThrowsDuplicateValueException(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    
    CreateUserDTO newUser = new CreateUserDTO("thibault", "thibault", UserRole.ADMIN);
    
    given(userRepository.addUser(newUser)).willThrow(DataIntegrityViolationException.class);
    assertThrows(DuplicateValueException.class, () ->{
      userService.addUser(newUser);
    });
  }
  
  @Test
  @DisplayName("When username, pw or role is missing, parametersMissingException is thrown.")
  void missingParametersInUserDTOThrowsMissingParametersException(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    CreateUserDTO newUser = new CreateUserDTO("thibault", null, UserRole.ADMIN);
    assertThrows(ParametersMissingException.class, () ->{
      userService.addUser(newUser);
    });
  }
  
  @Test
  @DisplayName("userRepository.updateUser() is called.")
  void updateUserHappyFlow(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    CreateUserDTO newUser = new CreateUserDTO("thibault", null, UserRole.ADMIN);
    User user = new User("thibault", "password", UserRole.ADMIN);
    
    given (userRepository.updateUser(1, newUser)).willReturn(user);
    
    User result = userService.updateUser(1, newUser);
    verify(userRepository).updateUser(1, newUser);
  }
  
  @Test
  @DisplayName("when ID doens't exist throw IdNotFoundException")
  void getUserByIdThrowsIdNotFoundExceptionForNonExistentId(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    
    given (userRepository.getUserById(5)).willThrow(EmptyResultDataAccessException.class);
    
    assertThrows(IdNotFoundException.class, () ->{
      userService.getUserById(5);
    });
  }
  
  @Test
  @DisplayName("Updating to an already taken username throws DuplicateValueException")
  void updateToTakenUsernameThrowsDuplicateValueException(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    
    CreateUserDTO updateUser = new CreateUserDTO("thibault", "thiabult", UserRole.ADMIN);
    
    given(userRepository.updateUser(5, updateUser)).willThrow(DuplicateKeyException.class);
    
    assertThrows(DuplicateValueException.class, () -> {
      userService.updateUser(5, updateUser);
    });
  }
  
  @Test
  @DisplayName("userRepository.delete is called.")
  void deleteUserHappyFlow(){
    UserRepository userRepository = mock(UserRepository.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserService userService = new UserService(userRepository, passwordEncoder);
    userService.deleteUser(5);
    verify(userRepository).deleteUser(5);
  }
}
