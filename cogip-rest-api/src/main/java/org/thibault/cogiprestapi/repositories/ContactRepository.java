package org.thibault.cogiprestapi.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thibault.cogiprestapi.model.Contact;

import java.sql.ResultSet;
import java.util.ArrayList;
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
  
  public List<Contact> getContactsByFilters(Integer id, String firstname, String lastname, String phone, Integer companyId){
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT * FROM contact WHERE 1=1");
    
    List<Object> reqParams = new ArrayList<>();
    
    if (id != null){
      sqlBuilder.append(" AND id= ?");
      reqParams.add(id);
    }
    
    if (firstname != null && !firstname.isEmpty()){
      sqlBuilder.append(" AND firstname = ?");
      reqParams.add(firstname);
    }
    
    if (lastname != null && !lastname.isEmpty()){
      sqlBuilder.append(" AND lastname= ?");
      reqParams.add(lastname);
    }
    
    if (phone != null && !phone.isEmpty()){
      sqlBuilder.append(" AND phone = ?");
      reqParams.add(phone);
    }
    
    if (companyId != null){
      sqlBuilder.append(" AND company_id= ?");
      reqParams.add(companyId);
    }
    return this.jdbc.query(sqlBuilder.toString(), getContactRowMapper(),reqParams.toArray());
  }
  
  
  private RowMapper<Contact> getContactRowMapper(){
    RowMapper<Contact> contactRowMapper = (ResultSet, i) ->{
      Contact rowObject = new Contact();
      rowObject.setId(ResultSet.getInt("id"));
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
