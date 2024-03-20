package org.thibault.cogiprestapi.repositories;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thibault.cogiprestapi.dto.CompanyDTO;
import org.thibault.cogiprestapi.enums.CompanyType;
import org.thibault.cogiprestapi.enums.converters.EnumConverter;
import org.thibault.cogiprestapi.model.Company;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepository {
  
  private final JdbcTemplate jdbc;
  
  public CompanyRepository (JdbcTemplate jdbc){
    this.jdbc = jdbc;
  }
  
  public List<CompanyDTO> getAllCompanies(){
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append(getAllCompaniesString());
    sqlBuilder.append(" ORDER BY id");
    
    List<Object> reqParams = new ArrayList<>();
    
    return getListOfCompanies(sqlBuilder.toString(), reqParams);
  }
  
  public Company getCompanyById(int id) throws EmptyResultDataAccessException {
    String sql = "SELECT * FROM company WHERE id= ?;";
    return jdbc.queryForObject(sql, getCompanyRowMapper(), id);
  }
  
  public List<CompanyDTO> searchCompaniesByFilters(Integer id, String name, String country, String vat, CompanyType type){
    
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append(getAllCompaniesString());
    sqlBuilder.append(" WHERE 1=1");
    
    List<Object> reqParams = new ArrayList<>();
    
    if (id != null){
      sqlBuilder.append(" AND id = ?");
      reqParams.add(id);
    }
    if (name != null && !name.isEmpty()){
      sqlBuilder.append(" AND name = ?");
      reqParams.add(name);
    }
    if (country != null && !country.isEmpty()){
      sqlBuilder.append(" AND country = ?");
      reqParams.add(country);
    }
    if (vat != null && !vat.isEmpty()){
      sqlBuilder.append(" AND vat = ?");
      reqParams.add(vat);
    }
    if (type != null){
      sqlBuilder.append(" AND company.type = ? ");
      reqParams.add(type.name());
    }
    
    sqlBuilder.append(" ORDER BY id;");
 
    return getListOfCompanies(sqlBuilder.toString(), reqParams);
  }
  
  public Company addCompany(Company company) throws DuplicateKeyException {
    String sql = "INSERT INTO company (name, country, vat, type) " +
            "VALUES (?, ?, ?, ?);";
    jdbc.update(sql, company.getName(), company.getCountry(), company.getVat(), company.getType().name());
    
    return jdbc.queryForObject("SELECT * FROM company WHERE vat=?;", getCompanyRowMapper(),company.getVat());
  }
  
  public void deleteCompany(int id){
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("DELETE FROM company WHERE 1=1 ");
    sqlBuilder.append("AND id= ?");
    
    jdbc.update(sqlBuilder.toString(), id);
  }
  
  public Company updateCompany(int id, Company company) throws EmptyResultDataAccessException{
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("UPDATE company");
    sqlBuilder.append(" SET name= ? , country= ?, vat= ?, type= ?");
    sqlBuilder.append(" WHERE id = ?");
    
    Company oldCompanyData = getCompanyById(id);
    
    String name = company.getName();
    String country = company.getCountry();
    String vat = company.getVat();
    CompanyType type = company.getType();
    
    if (name == null || name.isEmpty()) name = oldCompanyData.getName();
    if (country == null || country.isEmpty()) country = oldCompanyData.getCountry();
    if (vat == null || vat.isEmpty()) vat = oldCompanyData.getVat();
    if (type == null) type = oldCompanyData.getType();
    
    jdbc.update(sqlBuilder.toString(),
            name,
            country,
            vat,
            type.name(),
            id);
    
    String returnSql = "SELECT * FROM company where id = ?";
    return jdbc.query(returnSql, getCompanyRowMapper(), id).get(0);
  }
  
  private RowMapper<Company> getCompanyRowMapper(){
    
    RowMapper<Company> companyRowMapper = (resultSet, i ) -> {
      Company rowObject = new Company();
      rowObject.setId(resultSet.getInt("id"));
      rowObject.setName(resultSet.getString("name"));
      rowObject.setCountry(resultSet.getString("country"));
      rowObject.setVat(resultSet.getString("vat"));
      rowObject.setType(CompanyType.valueOf(resultSet.getString("type").toUpperCase()));
      
      return rowObject;
    };
    return companyRowMapper;
  }
  
  private List<CompanyDTO> getListOfCompanies(String sql){
    List<CompanyDTO> companies = new ArrayList<>();
    return companies;
  }
  
  private List<CompanyDTO> getListOfCompanies(String sql, List<Object> reqParams){
    List<CompanyDTO> companies = new ArrayList<>();
    
  this.jdbc.query(connection -> {
      PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      
      // Set parameters for the prepared statement
      int index = 1;
      for (Object reqParam : reqParams){
        preparedStatement.setObject(index++, reqParam);
      }
      return preparedStatement;
    },
    rs -> {
    do {
      CompanyDTO company = extractCompanyFromResultSet(rs);
      companies.add(company);
    } while (rs.next());
    }
            );
  return companies;
  }
  
  private CompanyDTO extractCompanyFromResultSet(ResultSet resultSet) throws SQLException {
    CompanyDTO company = new CompanyDTO();
    
    List<Integer> invoices = new ArrayList<>();
    List<String> contacts = new ArrayList<>();
    
    company.setId(resultSet.getInt("id"));
    company.setName(resultSet.getString("name"));
    company.setCountry(resultSet.getString("country"));
    company.setVat(resultSet.getString("vat"));
    
    int idCurrent = resultSet.getInt("id");
    int idNext = idCurrent;
    
    while (idCurrent == idNext){
      Integer invoiceNumber = resultSet.getInt("invoice_number");
      if (!resultSet.wasNull()) invoices.add(invoiceNumber);
      
      String contact = resultSet.getString("contact");
      if (!resultSet.wasNull()){
        if (!contacts.contains(resultSet.getString("contact"))){
          contacts.add(resultSet.getString("contact"));
        }
      }

      if (resultSet.next()){
        idNext = resultSet.getInt("id");
      }else{
        idNext = 0;
      }
      
      if (idCurrent != idNext){
        resultSet.previous();
      }
    }
    
    CompanyType companyType = new EnumConverter().convertStringToCompanyType(resultSet.getString("type"));
    company.setType(companyType);
    
    company.setInvoices(invoices);
    company.setContacts(contacts);
    
    return company;
  }
  
  private String getAllCompaniesString(){
    return "SELECT company.id, name, country, vat, company.type, invoice_number, concat(firstname, ' ', lastname) as contact FROM company " +
            "LEFT JOIN invoice ON company.id = invoice.company_id " +
            "LEFT JOIN contact ON company.id = contact.company_id";
  }
}