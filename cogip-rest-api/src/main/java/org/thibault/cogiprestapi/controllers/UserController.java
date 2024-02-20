package org.thibault.cogiprestapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class UserController {
  
  
  @GetMapping ("/users")
  public String getAllUsers(){
    return "returning all users";
  }
  
  
}