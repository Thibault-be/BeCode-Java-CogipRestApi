package org.thibault.cogiprestapi.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.thibault.cogiprestapi.dto.CreateUserDTO;
import org.thibault.cogiprestapi.dto.UserDTO;
import org.thibault.cogiprestapi.enums.UserRole;
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

  public User getUserById(int id){
    String sql = "SELECT * FROM user WHERE id= ?";
    User userById = jdbc.queryForObject(sql, getUserRowMapper(), id );
    return userById;
  }
  
  public User addUser(CreateUserDTO user){
    String sql = "INSERT INTO user (username, password, role)" +
            " VALUES ( ?, ?, ?)";
    
    jdbc.update(sql,
            user.getUsername(),
            user.getPassword(),
            user.getRole());
    
    String returnSql = "SELECT * FROM user WHERE username = ?";
    return jdbc.queryForObject(returnSql, getUserRowMapper(), user.getUsername());
  }
  
  public User updateUser(int id, CreateUserDTO createUserDTO){
    User userOldData = getUserById(id);
    String username = userOldData.getUsername();
    String password = userOldData.getPassword();
    String role = userOldData.getRole().name();
    
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("UPDATE user ");
    sqlBuilder.append("SET username= ?, password= ?, role= ? ");
    sqlBuilder.append("WHERE id= ?");
    
    if (createUserDTO.getUsername() != null && !createUserDTO.getUsername().isEmpty()){
      username = createUserDTO.getUsername();
    }
    
    if(createUserDTO.getPassword() != null && !createUserDTO.getPassword().isEmpty()){
      password = createUserDTO.getPassword();
    }
    
    if(createUserDTO.getRole() != null){
      role = createUserDTO.getRole().name();
    }
    
    jdbc.update(sqlBuilder.toString(), username, password, role, id);

    String sqlUpdatedUser = "SELECT * FROM user WHERE id = ?";
    return jdbc.queryForObject(sqlUpdatedUser, getUserRowMapper(), id);
  }
  
  public void deleteUser(int id){
    String sql = "DELETE FROM user WHERE id=?";
    
    jdbc.update(sql, id);
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
