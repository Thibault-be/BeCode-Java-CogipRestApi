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
    String sql = "SELECT * FROM user;";
    return jdbc.query(sql, getUserRowMapper());
  }
  
  public User getUserById(String id){
    String sql = "SELECT * FROM user WHERE id= ?";
    User userById = jdbc.queryForObject(sql, getUserRowMapper(), id );
    return userById;
  }
  
  public void addUser(User user){
    String sql = "INSERT INTO user (username, password, role)" +
            " VALUES ( ?, ?, ?)";
    
    jdbc.update(sql,
            user.getUsername(),
            user.getPassword(),
            user.getRole());
  }
  
  public User updateUser(int id, User user){
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("UPDATE user ");
    sqlBuilder.append("SET username= ?, password= ?, role= ? ");
    sqlBuilder.append("WHERE id= ?");
    
    jdbc.update(sqlBuilder.toString(), user.getUsername(), user.getPassword(), user.getRole(), id);
    
    String sqlUpdatedCompany = "SELECT * FROM user WHERE id = ?";
    
    return jdbc.query(sqlUpdatedCompany, getUserRowMapper(), id).get(0);
    
  }
  
  private RowMapper<User> getUserRowMapper(){
    RowMapper<User> userRowMapper = (resultSet, i) -> {
      
      System.out.println("in the rowmapper");
      
      User rowObject = new User("", "","");
      rowObject.setId(resultSet.getInt("id"));
      rowObject.setUserName(resultSet.getString("username"));
      rowObject.setPassword(resultSet.getString("password"));
      rowObject.setRole(resultSet.getString("role"));
      
      return rowObject;
    };
    return userRowMapper;
  }
}
