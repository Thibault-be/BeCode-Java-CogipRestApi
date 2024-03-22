package org.thibault.cogiprestapi.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thibault.cogiprestapi.dto.CreateUserDTO;
import org.thibault.cogiprestapi.enums.UserRole;
import org.thibault.cogiprestapi.exceptions.DuplicateValueException;
import org.thibault.cogiprestapi.exceptions.IdNotFoundException;
import org.thibault.cogiprestapi.exceptions.ParametersMissingException;
import org.thibault.cogiprestapi.exceptions.ResultSetEmptyException;
import org.thibault.cogiprestapi.model.User;
import org.thibault.cogiprestapi.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
  
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<User> getAllUsers(){
    
    List<User> allUsers = this.userRepository.getAllUsers();
    if (allUsers.isEmpty()) throw new ResultSetEmptyException("There are no users in the database.");
    return allUsers;
  }
  
  public User getUserById(int id){
    try{
      return this.userRepository.getUserById(id);
    } catch (EmptyResultDataAccessException ex){
      throw new IdNotFoundException("The user with id " + id + " could not be found.");
    }
  }
  
  public List<User> getUsersByFilters(Integer id, String username, UserRole role){
    List<User> filteredUsers = userRepository.getUsersByFilters(id, username, role);
    if (filteredUsers.isEmpty()) throw new ResultSetEmptyException("No matches for your search criteria.");
    return filteredUsers;
  }
  
  public User addUser(CreateUserDTO createUserDTO){
    String allFields = parametersMissing(createUserDTO);
    if (allFields != null) throw new ParametersMissingException(allFields);
    try{
      String passwordToEncode = createUserDTO.getPassword();
      String encodedPassword = this.passwordEncoder.encode(passwordToEncode);
      createUserDTO.setPassword(encodedPassword);
      return this.userRepository.addUser(createUserDTO);
    } catch (DataIntegrityViolationException dive){
      throw new DuplicateValueException("The username " + createUserDTO.getUsername() + " is already taken. Please choose another username.");
    }
  }
  
  public User updateUser(int id, CreateUserDTO createUserDTO){
    try{
      if (createUserDTO.getPassword() != null){
        String passwordToEncode = createUserDTO.getPassword();
        String encodedPassword = this.passwordEncoder.encode(passwordToEncode);
        createUserDTO.setPassword(encodedPassword);
      }
      return this.userRepository.updateUser(id, createUserDTO);
    } catch (EmptyResultDataAccessException mtre){
      throw new IdNotFoundException("User with id " + id + " could not be found.");
    } catch (DuplicateKeyException dke){
      throw new DuplicateValueException("A user with the username " + createUserDTO.getUsername() + " already exists.");
    }
  }
  
  public void deleteUser(int id){
    try{
      this.userRepository.deleteUser(id);
    } catch (EmptyResultDataAccessException ex){
      System.out.println("catching");
      throw new IdNotFoundException("The user with id " + id + " could not be found and could not be deleted.");
    }
  }
  
  String parametersMissing(CreateUserDTO user){
    StringBuilder missingParams = new StringBuilder("These are the missing parameters:\n");
    boolean flag = false;
    
    if (user.getUsername() == null || user.getUsername().isEmpty()){
      missingParams.append("username\n");
      flag = true;
    }
    if (user.getPassword() == null || user.getPassword().isEmpty()){
      missingParams.append("password\n");
      flag = true;
    }
    if (user.getRole() == null){
      missingParams.append("role");
      flag = true;
    }
    if (flag) return missingParams.toString();
    return null;
  }
}
