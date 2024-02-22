package org.thibault.cogiprestapi.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thibault.cogiprestapi.model.Contact;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class ContactRepository {
  
  private final JdbcTemplate jdbc;
  
  public ContactRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }
  
  public List<Contact> getAllContacts(){
    String sql = "SELECT * FROM contact;";
    
    return jdbc.query(sql, getContactRowMapper()) ;
  }
  
  
  private RowMapper<Contact> getContactRowMapper(){
    RowMapper<Contact> contactRowMapper = (ResultSet, i) ->{
      Contact rowObject = new Contact();
      rowObject.setFirstname(ResultSet.getString("firstname"));
      rowObject.setLastname(ResultSet.getString("lastname"));
      rowObject.setPhone(ResultSet.getString("phone"));
      rowObject.setEmail(ResultSet.getString("email"));
      rowObject.setCompanyId(ResultSet.getInt("company_id"));
      
      return rowObject;
    };
    return contactRowMapper;
  }
}
