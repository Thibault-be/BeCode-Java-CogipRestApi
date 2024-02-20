package org.thibault.cogiprestapi.services;


import org.springframework.stereotype.Service;
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
  
}
