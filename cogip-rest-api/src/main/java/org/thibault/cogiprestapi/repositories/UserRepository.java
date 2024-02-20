package org.thibault.cogiprestapi.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.thibault.cogiprestapi.model.User;

import java.util.List;

@Repository
public class UserRepository {
  
  private final JdbcTemplate jdbc;
  
  public UserRepository(JdbcTemplate jdbc){
    this.jdbc = jdbc;
  }
  
  public List<User> getAllUsers(){
    String query = "SELECT * FROM user";
    
    RowMapper<User> userRowMapper = (r , i) -> {
      User rowObject = new User();
      rowObject.setId(r.getInt("id"));
      rowObject.setUserName(r.getString("username"));
      rowObject.setPassword(r.getString("password"));
      rowObject.setRole(r.getString("role"));
      
      return rowObject;
    };
    return jdbc.query(query, userRowMapper);
  }
}
