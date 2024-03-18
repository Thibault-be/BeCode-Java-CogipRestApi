package org.thibault.cogiprestapi.repositories;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.thibault.cogiprestapi.dto.CreateUserDTO;
import org.thibault.cogiprestapi.enums.UserRole;
import org.thibault.cogiprestapi.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

  public User getUserById(int id) throws EmptyResultDataAccessException {
    String sql = "SELECT * FROM user WHERE id= ?";
    User userById = jdbc.queryForObject(sql, getUserRowMapper(), id );
    return userById;
  }
  
  public Optional<User> findByUsername(String username ){
    String sql = "SELECT * FROM user where username= ?";
    Optional<User> user = Optional.ofNullable(jdbc.queryForObject(sql, getUserRowMapper(), username));
    return user;
  }
  
  public List<User> getUsersByFilters(Integer id, String username, UserRole role){
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT * FROM user WHERE 1+1");
    
    ArrayList<Object> params = new ArrayList<>();
    
    if (id != null){
      sqlBuilder.append(" AND id = ?");
      params.add(id);
    }
    if (username != null && !username.isEmpty()){
      sqlBuilder.append(" AND username = ?");
      params.add(username);
    }
    if (role != null){
      sqlBuilder.append(" AND role =?");
      params.add(role.name().toUpperCase());
    }
    sqlBuilder.append(";");
    return jdbc.query(sqlBuilder.toString(), getUserRowMapper(), params.toArray());
  }
  
  public User addUser(CreateUserDTO user) throws DataIntegrityViolationException {

    String sql = "INSERT INTO user (username, password, role)" +
            " VALUES ( ?, ?, ?)";
    
      jdbc.update(sql,
              user.getUsername(),
              user.getPassword(),
              user.getRole().name());
    
    String returnSql = "SELECT * FROM user WHERE username = ?";
    return jdbc.queryForObject(returnSql, getUserRowMapper(), user.getUsername());
  }
  
  public User updateUser(int id, CreateUserDTO createUserDTO) throws EmptyResultDataAccessException , DuplicateKeyException {
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
    User userExists = getUserById(id);
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
