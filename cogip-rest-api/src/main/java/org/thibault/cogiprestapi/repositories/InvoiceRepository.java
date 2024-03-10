package org.thibault.cogiprestapi.repositories;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.stereotype.Repository;
import org.thibault.cogiprestapi.enums.Currency;
import org.thibault.cogiprestapi.enums.InvoiceStatus;
import org.thibault.cogiprestapi.enums.InvoiceType;
import org.thibault.cogiprestapi.model.Invoice;

import java.math.BigDecimal;
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
  
  public Invoice getInvoiceById(int id) throws EmptyResultDataAccessException {
    String sql = "SELECT * FROM invoice where id= ?";
    return jdbc.queryForObject(sql, getInvoiceRowMapper(),id);
  }
  
  public List<Invoice> searchInvoicesByFilters(Integer id, Integer companyId, String invoiceNumber, Currency currency, InvoiceType type, InvoiceStatus status){
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
    if (currency != null){
      sqlBuilder.append(" AND currency = ?");
      reqParams.add(currency.name());
    }
    if (type != null){
      sqlBuilder.append(" AND type = ?");
      reqParams.add(type.name());
    }
    if (status != null){
      sqlBuilder.append(" AND status = ?");
      reqParams.add(status.name());
    }
    return jdbc.query(sqlBuilder.toString(), getInvoiceRowMapper(), reqParams.toArray());
  }
  
  public void addInvoice(Invoice invoice) throws DataIntegrityViolationException {
    StringBuilder sqlBuilder = new StringBuilder();
    
    sqlBuilder.append("INSERT INTO invoice (company_id, contact_id, invoice_number, value, currency, type, status)");
    sqlBuilder.append("VALUES (?,?,?,?,?,?,?);");
    
    jdbc.update(sqlBuilder.toString(),
            invoice.getCompanyId(),
            invoice.getContactId(),
            invoice.getInvoiceNumber(),
            invoice.getValue(),
            invoice.getCurrency().name(),
            invoice.getType().name(),
            invoice.getStatus().name());
  }
  
  public Invoice updateInvoice(int id, Invoice invoice) throws DataIntegrityViolationException {
    String sql = "UPDATE invoice " +
            "SET company_id = ?, contact_id = ?, invoice_number = ?, value= ?, currency= ?, type= ?, status= ?  " +
            "WHERE id = ?";
    
    Invoice oldInvoiceData = getInvoiceById(id);
    
    Integer companyId = invoice.getCompanyId();
    Integer contactId = invoice.getContactId();
    String invoiceNumber = invoice.getInvoiceNumber();
    BigDecimal value = invoice.getValue();
    Currency currency = invoice.getCurrency();
    InvoiceType type = invoice.getType();
    InvoiceStatus status = invoice.getStatus();
    
    if (companyId == null || companyId == 0) companyId = oldInvoiceData.getCompanyId();
    if (contactId == null || contactId == 0) contactId = oldInvoiceData.getContactId();
    if (invoiceNumber == null || invoiceNumber.isEmpty()) invoiceNumber = oldInvoiceData.getInvoiceNumber();
    if (value == null) value = oldInvoiceData.getValue();
    if (currency == null) currency = oldInvoiceData.getCurrency();
    if (type == null) type = oldInvoiceData.getType();
    if (status == null) status = oldInvoiceData.getStatus();
    
    jdbc.update(sql,
                companyId,
                contactId,
                invoiceNumber,
                value,
                currency.name(),
                type.name(),
                status.name(),
                id
            );
    
    String updatedInvoice = "SELECT * from invoice WHERE id = ?";
    return jdbc.queryForObject(updatedInvoice, getInvoiceRowMapper(), id);
  }
  
  private RowMapper<Invoice> getInvoiceRowMapper(){
    RowMapper<Invoice> invoiceMapper = (ResultSet, i) ->{
      Invoice rowObject = new Invoice();
      rowObject.setId(ResultSet.getInt("id"));
      rowObject.setCompanyId(ResultSet.getInt("company_id"));
      rowObject.setContactId(ResultSet.getInt("contact_id"));
      rowObject.setInvoiceNumber(ResultSet.getString("invoice_number"));
      rowObject.setValue(ResultSet.getBigDecimal("value"));
      rowObject.setCurrency(Currency.valueOf(ResultSet.getString("currency")));
      rowObject.setType(InvoiceType.valueOf(ResultSet.getString("type").toUpperCase()));
      rowObject.setStatus(InvoiceStatus.valueOf(ResultSet.getString("status").toUpperCase()));
      
      return rowObject;
    };
    return invoiceMapper;
  }
}
