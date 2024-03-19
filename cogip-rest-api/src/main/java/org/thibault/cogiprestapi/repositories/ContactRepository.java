package org.thibault.cogiprestapi.repositories;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thibault.cogiprestapi.dto.ContactDTO;
import org.thibault.cogiprestapi.model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContactRepository {
  
  private final JdbcTemplate jdbc;
  
  public ContactRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }
  
  public List<ContactDTO> getAllContacts() {
    String sql = getAllContactsString();
    List<ContactDTO> contacts = new ArrayList<>();
    
    List<Object> reqParams = new ArrayList<>();
    
    return getListOfContacts(sql, reqParams);
  }
  
  public Contact getContactById(int id) throws EmptyResultDataAccessException {
    String sql = "SELECT * FROM contact where id = ?";
    return jdbc.queryForObject(sql, getContactRowMapper(), id);
  }
  
  public List<ContactDTO> getContactsByFilters(Integer id, String firstname, String lastname, String phone, Integer companyId){
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append(getAllContactsString());
    sqlBuilder.append(" WHERE 1=1");
    
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
    sqlBuilder.append(";");
    return getListOfContacts(sqlBuilder.toString(), reqParams);
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
  
  private List<ContactDTO> getListOfContacts(String sql, List<Object> reqParams){
    List<ContactDTO> contacts = new ArrayList<>();
    
    this.jdbc.query(connection -> {
              PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
              
              // Set parameters for the prepared statement
              int index = 1;
              for (Object reqParam : reqParams) {
                preparedStatement.setObject(index++, reqParam);
              }
              return preparedStatement;
            },
            rs -> {
              do {
                ContactDTO contact = extractContactFromResultSet(rs);
                contacts.add(contact);
              } while (rs.next());
            }
    );
    return contacts;
  }
  
  private ContactDTO extractContactFromResultSet(ResultSet resultSet) throws SQLException {
    ContactDTO contact = new ContactDTO();
    contact.setFirstname(resultSet.getString("firstname"));
    contact.setLastname(resultSet.getString("lastname"));
    contact.setEmail(resultSet.getString("email"));
    contact.setPhone(resultSet.getString("phone"));
    contact.setCompanyName(resultSet.getString("company_name"));
    
    List<Integer> invoices = new ArrayList<>();
    
    int idCurrent = resultSet.getInt("id");
    int idNext = idCurrent;

    while (idCurrent == idNext) {
      invoices.add(resultSet.getInt("invoice_number"));
      if (resultSet.next()) {
        idNext = resultSet.getInt("id");
      } else {
        idNext = 0;
      }
      
      if (idCurrent != idNext) {
        resultSet.previous();
      }
    }
    contact.setInvoiceNumbers(invoices);
    return contact;
  }
  
  private String getAllContactsString(){
    return "SELECT contact.id, contact.firstname, contact.lastname, contact.email, contact.phone," +
            " company.name AS company_name, invoice.invoice_number FROM contact " +
            "INNER JOIN company ON contact.company_id = company.id " +
            "INNER JOIN invoice ON contact.company_id = invoice.company_id";
  }
}
