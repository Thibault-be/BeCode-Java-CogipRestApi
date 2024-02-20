package org.thibault.cogiprestapi.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.thibault.cogiprestapi.model.User;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class UserRepository {
  
  private final JdbcTemplate jdbc;
  
  public UserRepository(JdbcTemplate jdbc){
    this.jdbc = jdbc;
  }
  
  public List<User> getAllUsers(){
    String query = "SELECT * FROM user;";
    return jdbc.query(query, getUserRowMapper());
  }
  
  public User getUserById(String id){
    String query = "SELECT * FROM user WHERE id= ?";
    User userById = jdbc.queryForObject(query, getUserRowMapper(), id );
    return userById;
  }
  
  private RowMapper<User> getUserRowMapper(){
    RowMapper<User> userRowMapper = (resultSet, i) -> {
      User rowObject = new User();
      rowObject.setId(resultSet.getInt("id"));
      rowObject.setUserName(resultSet.getString("username"));
      rowObject.setPassword(resultSet.getString("password"));
      rowObject.setRole(resultSet.getString("role"));
      
      return rowObject;
    };
    return userRowMapper;
  }
}
