package org.thibault.cogiprestapi.repositories;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thibault.cogiprestapi.dto.InvoiceDTO;
import org.thibault.cogiprestapi.enums.CompanyType;
import org.thibault.cogiprestapi.enums.Currency;
import org.thibault.cogiprestapi.enums.InvoiceStatus;
import org.thibault.cogiprestapi.enums.InvoiceType;
import org.thibault.cogiprestapi.enums.converters.EnumConverter;
import org.thibault.cogiprestapi.model.Invoice;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InvoiceRepository {
  
  private final JdbcTemplate jdbc;
  
  public InvoiceRepository(JdbcTemplate jdbc){
    this.jdbc = jdbc;
  }
  
  public List<InvoiceDTO> getAllInvoices(){
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append(getAllInvoicesString());
    sqlBuilder.append(";");
    List<Object> reqParams = new ArrayList<>();
    return getListOfInvoices(sqlBuilder.toString(), reqParams);
  }
  
  public Invoice getInvoiceById(int id) throws EmptyResultDataAccessException {
    String sql = "SELECT * FROM invoice where id= ?";
    return jdbc.queryForObject(sql, getInvoiceRowMapper(),id);
  }
  
  public List<InvoiceDTO> searchInvoicesByFilters(Integer id, Integer companyId, String invoiceNumber, Currency currency, InvoiceType type, InvoiceStatus status){
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append(getAllInvoicesString());
    sqlBuilder.append(" WHERE 1=1");
    
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
    return getListOfInvoices(sqlBuilder.toString(), reqParams);
  }
  
  public Invoice addInvoice(Invoice invoice) throws DataIntegrityViolationException {
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
    
    return jdbc.queryForObject("SELECT * FROM invoice WHERE company_id= ? AND invoice_number= ?;",
            getInvoiceRowMapper(), invoice.getCompanyId(), invoice.getInvoiceNumber());
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
  
  private List<InvoiceDTO> getListOfInvoices(String sql, List<Object> reqParams){
    List<InvoiceDTO> invoices = new ArrayList<>();
    
    this.jdbc.query(connection -> {
      PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      
      //set parameters for the prepared statement
      int index = 1;
      for (Object reqParam : reqParams){
        preparedStatement.setObject(index++, reqParam);
      }
      return preparedStatement;
    },
    rs -> {
      do{
        InvoiceDTO invoice = extractInvoiceFromResultSet(rs);
        invoices.add(invoice);
      } while (rs.next());
    }
  );
  return invoices;
  }
  
  private InvoiceDTO extractInvoiceFromResultSet(ResultSet resultSet) throws SQLException {
    InvoiceDTO invoice = new InvoiceDTO();
    invoice.setInvoiceNumber(resultSet.getInt("invoice_number"));
    invoice.setValue(resultSet.getBigDecimal("value"));
    invoice.setCompanyName(resultSet.getString("company"));
    invoice.setContact(resultSet.getString("contact"));
    invoice.setTimestamp(resultSet.getTimestamp("created_on"));
    
    Currency currency = new EnumConverter().converStringToCurrency(
            resultSet.getString("currency"));
    invoice.setCurrency(currency);
    
    InvoiceStatus status = new EnumConverter().convertStringToInvoiceStatus(
            resultSet.getString("status"));
    invoice.setStatus(status);
    
    CompanyType type = new EnumConverter().convertStringToCompanyType(
            resultSet.getString("type"));
    invoice.setCompanyType(type);
    
    return invoice;
  }
  
  private String getAllInvoicesString(){
    return "SELECT invoice_number, value, currency, status, company.name as company, company.type," +
            " concat(contact.firstname, \" \", contact.lastname) as contact, created_on " +
            "FROM invoice " +
            "INNER JOIN company on company_id = company.id " +
            "INNER JOIN contact on contact_id = contact.id";
  }
}
