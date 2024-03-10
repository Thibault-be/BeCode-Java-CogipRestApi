package org.thibault.cogiprestapi.repositories;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thibault.cogiprestapi.enums.CompanyType;
import org.thibault.cogiprestapi.model.Company;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepository {
  
  private final JdbcTemplate jdbc;
  
  public CompanyRepository (JdbcTemplate jdbc){
    this.jdbc = jdbc;
  }
  
  public List<Company> getAllCompanies(){
    String sql = "SELECT * FROM company;";
    return jdbc.query(sql, getCompanyRowMapper());
  }
  
  public Company getCompanyById(int id) throws EmptyResultDataAccessException {
    String sql = "SELECT * FROM company WHERE id= ?;";
    return jdbc.queryForObject(sql, getCompanyRowMapper(), id);
  }
  
  public List<Company> searchCompaniesByFilters(Integer id, String name, String country, String vat, CompanyType type){
    
    List<Object> paramsArray = new ArrayList<>();
    
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT * FROM company WHERE 1=1");
    
    if (id != null){
      sqlBuilder.append(" AND id = ?");
      paramsArray.add(id);
    }
    if (name != null && !name.isEmpty()){
      sqlBuilder.append(" AND name = ?");
      paramsArray.add(name);
    }
    if (country != null && !country.isEmpty()){
      sqlBuilder.append(" AND country = ?");
      paramsArray.add(country);
    }
    if (vat != null && !vat.isEmpty()){
      sqlBuilder.append(" AND vat = ?");
      paramsArray.add(vat);
    }
    if (type != null){
      sqlBuilder.append(" AND type = ? ");
      paramsArray.add(type.name());
    }
    return jdbc.query(sqlBuilder.toString(), getCompanyRowMapper(), paramsArray.toArray());
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
}
