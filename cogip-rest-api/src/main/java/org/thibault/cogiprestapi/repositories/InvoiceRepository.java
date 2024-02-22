package org.thibault.cogiprestapi.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thibault.cogiprestapi.model.Invoice;

import java.util.ArrayList;
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
  
  public List<Invoice> searchInvoicesByFilters(Integer id, Integer companyId, String invoiceNumber, String type, String status){
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT * FROM invoice WHERE 1=1");
    
    List<Object> reqParams = new ArrayList<>();
    
    if (id != null){
      sqlBuilder.append(" AND id = ?");
      reqParams.add(id);
    }
    
    if (companyId != null){
      sqlBuilder.append(" AND company_id = ?");
      reqParams.add(companyId);
    }
    
    if (invoiceNumber != null && !invoiceNumber.isEmpty()){
      sqlBuilder.append(" AND invoice_number = ?");
      reqParams.add(invoiceNumber);
    }
    
    if (type != null && !type.isEmpty()){
      sqlBuilder.append(" AND type = ?");
      reqParams.add(type);
    }
    
    if (status != null && !status.isEmpty()){
      sqlBuilder.append(" AND status = ?");
      reqParams.add(status);
    }
    
    return jdbc.query(sqlBuilder.toString(), getInvoiceRowMapper(), reqParams.toArray());
  }
  
  public void addInvoice(Invoice invoice){
    StringBuilder sqlBuilder = new StringBuilder();
    
    sqlBuilder.append("INSERT INTO invoice (company_id, contact_id, invoice_number, value, currency, type, status)");
    sqlBuilder.append("VALUES (?,?,?,?,?,?,?);");

    jdbc.update(sqlBuilder.toString(),
            invoice.getCompanyId(),
            invoice.getContactId(),
            invoice.getInvoiceNumber(),
            invoice.getValue(),
            invoice.getCurrency(),
            invoice.getType(),
            invoice.getStatus());
  }
  
  public Invoice updateInvoice(int id, Invoice invoice){
    String sql = "UPDATE invoice " +
            "SET company_id = ?, contact_id = ?, invoice_number = ?, value= ?, currency= ?, type= ?, status= ?  " +
            "WHERE id = ?";
    
    jdbc.update(sql,
                invoice.getCompanyId(),
                invoice.getContactId(),
                invoice.getInvoiceNumber(),
                invoice.getValue(),
                invoice.getCurrency(),
                invoice.getType(),
                invoice.getStatus(),
                id
            );
    
    String updatedInvoice = "SELECT * from invoice WHERE id = ?";
    return jdbc.query(updatedInvoice, getInvoiceRowMapper(), id).get(0);
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
