package org.thibault.cogiprestapi.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.thibault.cogiprestapi.dto.CreateUserDTO;
import org.thibault.cogiprestapi.dto.UserDTO;
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
  
  public UserService(UserRepository userRepository){
    this.userRepository = userRepository;
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
  
  public User addUser(CreateUserDTO createUserDTO){
    String allFields = parametersMissing(createUserDTO);
    if (allFields != null) throw new ParametersMissingException(allFields);
    
    try{
      return this.userRepository.addUser(createUserDTO);
    } catch (DataIntegrityViolationException dive){
      throw new DuplicateValueException("The username " + createUserDTO.getUsername() + " is already taken. Please choose another username.");
    }
  }
  
  public User updateUser(int id, CreateUserDTO createUserDTO){
    try{
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
      throw new IdNotFoundException("The user with id " + id + " could not be found and could not be deleted.");
    }
  }
  
  private String parametersMissing(CreateUserDTO user){
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
