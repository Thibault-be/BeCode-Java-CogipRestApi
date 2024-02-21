package org.thibault.cogiprestapi.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thibault.cogiprestapi.model.Invoice;

import java.util.List;

@Repository
public class InvoiceRepository {
  
  private final JdbcTemplate jdbc;
  
  public InvoiceRepository(JdbcTemplate jdbc){
    this.jdbc = jdbc;
  }
  
  public List<Invoice> getAllInvoices(){
    String sql = "SELECT * FROM invoice";
    return jdbc.query(sql, getInvoiceRowMapper());
  }
  
  private RowMapper<Invoice> getInvoiceRowMapper(){
    
    RowMapper<Invoice> invoiceMapper = (ResultSet, i) ->{
      Invoice rowObject = new Invoice();
      rowObject.setId(ResultSet.getInt("id"));
      rowObject.setCompanyId(ResultSet.getInt("company_id"));
      rowObject.setContactId(ResultSet.getInt("contact_id"));
      rowObject.setInvoiceNumber(ResultSet.getString("invoice_number"));
      rowObject.setValue(ResultSet.getBigDecimal("value"));
      rowObject.setCurrency(ResultSet.getString("currency"));
      rowObject.setType(ResultSet.getString("type"));
      rowObject.setStatus(ResultSet.getString("status"));
      
      return rowObject;
    };
    return invoiceMapper;
  }
}
