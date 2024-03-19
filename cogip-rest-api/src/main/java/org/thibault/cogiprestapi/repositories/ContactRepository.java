package org.thibault.cogiprestapi.repositories;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thibault.cogiprestapi.dto.ContactDTO;
import org.thibault.cogiprestapi.model.Contact;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContactRepository {
  
  private final JdbcTemplate jdbc;
  
  public ContactRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }
  
//  public List<Contact> getAllContacts(){
//    String sql = "SELECT * FROM contact;";
//    return jdbc.query(sql, getContactRowMapper()) ;
//  }
  
  public List<ContactDTO> getAllContacts(){
    String sql = "SELECT contact.firstname, contact.lastname, contact.email, contact.phone," +
            " company.name AS company_name, invoice.invoice_number FROM contact " +
            "INNER JOIN company ON contact.company_id = company.id " +
            "INNER JOIN invoice ON contact.company_id = invoice.company_id;";
    
    return jdbc.query(sql, getContactDTORowMapper());
  }
  
  public Contact getContactById(int id) throws EmptyResultDataAccessException {
    String sql = "SELECT * FROM contact where id = ?";
    return jdbc.queryForObject(sql, getContactRowMapper(), id);
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
  
  public Contact addContact(Contact contact){
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("INSERT INTO contact (firstname, lastname, phone, email, company_id)");
    sqlBuilder.append(" VALUES (?,?,?,?,?);");
    
    jdbc.update(sqlBuilder.toString(),
            contact.getFirstname(),
            contact.getLastname(),
            contact.getPhone(),
            contact.getEmail(),
            contact.getCompanyId());
    
    return jdbc.queryForObject("SELECT * FROM contact WHERE firstname = ? AND lastname= ? AND company_id= ?",
            getContactRowMapper(),
            contact.getFirstname(), contact.getLastname(), contact.getCompanyId());
  }
  
  public Contact updateContact(int id, Contact contact) throws EmptyResultDataAccessException, DataIntegrityViolationException{
    
    Contact oldContactData = getContactById(id);
    
    String firstname = contact.getFirstname();
    String lastname = contact.getLastname();
    String phone = contact.getPhone();
    String email = contact.getEmail();
    Integer companyId = contact.getCompanyId();
    
    if (firstname == null || firstname.isEmpty()) firstname = oldContactData.getFirstname();
    if (lastname == null || lastname.isEmpty()) lastname = oldContactData.getLastname();
    if (phone == null || phone.isEmpty()) phone = oldContactData.getPhone();
    if (email == null || email.isEmpty()) email = oldContactData.getEmail();
    if (companyId == null || companyId == 0) companyId = oldContactData.getCompanyId();
    
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("UPDATE contact");
    sqlBuilder.append(" SET firstname = ?, lastname = ?, phone = ?, email = ?, company_id = ?");
    sqlBuilder.append(" WHERE id= ?");
    
    this.jdbc.update(sqlBuilder.toString(),
            firstname,
            lastname,
            phone,
            email,
            companyId,
            id);
    
    return this.jdbc.queryForObject("SELECT * FROM contact WHERE id=?", getContactRowMapper(), id);
  }
  
  public void deleteContact(int id) throws EmptyResultDataAccessException {
    Contact contactExists = getContactById(id);
    this.jdbc.update("DELETE FROM contact WHERE id= ?", id);
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
  
  private RowMapper<ContactDTO> getContactDTORowMapper(){
    RowMapper<ContactDTO> contactDTORowMapper = (ResultSet, i) -> {
      ContactDTO rowObject = new ContactDTO();
      
      List<Integer> invoices = new ArrayList<>();
      
      rowObject.setFirstname(ResultSet.getString("firstname"));
      rowObject.setLastname(ResultSet.getString("lastname"));
      rowObject.setEmail(ResultSet.getString("email"));
      rowObject.setPhone(ResultSet.getString("phone"));
      rowObject.setCompanyName(ResultSet.getString("company_name"));
      
      String companyOriginal = ResultSet.getString("company_name");
      String companyNext = "";
      while (companyOriginal.equals(companyNext)){
        invoices.add(ResultSet.getInt("invoice_number"));
        ResultSet.next();
        companyNext = ResultSet.getString("company_name");
      }
      
      rowObject.setInvoiceNumbers(invoices);
      
      return rowObject;
    };
    return contactDTORowMapper;
  }
  
}
