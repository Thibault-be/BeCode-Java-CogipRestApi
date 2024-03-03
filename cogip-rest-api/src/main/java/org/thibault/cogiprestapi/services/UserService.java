package org.thibault.cogiprestapi.services;


import org.springframework.stereotype.Service;
import org.thibault.cogiprestapi.dto.CreateUserDTO;
import org.thibault.cogiprestapi.dto.UserDTO;
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
    return this.userRepository.getAllUsers();
  }
  
  public User getUserById(int id){
    return this.userRepository.getUserById(id);
  }
  
  public User addUser(CreateUserDTO createUserDTO){
    return this.userRepository.addUser(createUserDTO);
  }
  
  public User updateUser(int id, CreateUserDTO createUserDTO){
    return this.userRepository.updateUser(id, createUserDTO);
  }
  
  public void deleteUser(int id){
    this.userRepository.deleteUser(id);
  }
}
