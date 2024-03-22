package org.thibault.cogiprestapi.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.thibault.cogiprestapi.enums.UserRole;
import org.thibault.cogiprestapi.model.User;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

public class UserRepositoryTest {
  
  @Test
  @DisplayName("Verifies getting all users")
  void getAllUsersHappyFlow(){
    JdbcTemplate jdbc = mock(JdbcTemplate.class);
    UserRepository userRepository = new UserRepository(jdbc);
    
    List<User> expectedUsers = Arrays.asList(
            new User("user1", "password1", UserRole.INTERN),
            new User("user2", "password2", UserRole.ADMIN)
    );
    
    given(jdbc.query("someString", getUserRowMapper())).willReturn(expectedUsers);
    
    List<User> actualUsers = userRepository.getAllUsers();
    
    assertFalse(actualUsers.isEmpty(), "List is not empty.");
    
    for (User user : actualUsers) {
      assertNotNull(user.getId(), "User ID is not null");
      assertNotNull(user.getUsername(), "Username is not null");
      assertNotNull(user.getPassword(), "Password is not null");
      assertNotNull(user.getRole(), "User role is not null");
    }
  }
  
  
  private RowMapper<User> getUserRowMapper(){
    RowMapper<User> userRowMapper = (resultSet, i) -> {
      
      User rowObject = new User();
      rowObject.setId(resultSet.getInt("id"));
      rowObject.setUsername(resultSet.getString("username"));
      rowObject.setPassword(resultSet.getString("password"));
      rowObject.setRole(UserRole.valueOf(resultSet.getString("role")));
      
      return rowObject;
    };
    return userRowMapper;
  }

}
