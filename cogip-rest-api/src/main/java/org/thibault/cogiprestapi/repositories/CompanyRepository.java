package org.thibault.cogiprestapi.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
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
  
  public List<Company> searchCompaniesByFilters(String name, String country, String type){
    
    List<Object> paramsArray = new ArrayList<>();
    
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("SELECT * FROM company WHERE 1=1");
    
    if (name != null && !name.isEmpty()){
      //sqlBuilder.append(" AND name = '" + name +"'");
      sqlBuilder.append(" AND name = ?");
      paramsArray.add(name);
    }
    
    if (country != null && !country.isEmpty()){
      sqlBuilder.append(" AND country = ?");
      paramsArray.add(country);
    }
    
    if (type != null && !type.isEmpty()){
      sqlBuilder.append(" AND type = ? ");
      paramsArray.add(type);
    }
    
    return jdbc.query(sqlBuilder.toString(), paramsArray.toArray(), getCompanyRowMapper());
  }
  
  private RowMapper<Company> getCompanyRowMapper(){
    
    RowMapper<Company> companyRowMapper = (resultSet, i ) -> {
      Company rowObject = new Company("","","","");
      rowObject.setId(resultSet.getInt("id"));
      rowObject.setName(resultSet.getString("name"));
      rowObject.setCountry(resultSet.getString("country"));
      rowObject.setVat(resultSet.getString("vat"));
      rowObject.setType(resultSet.getString("type"));
      
      return rowObject;
    };
    return companyRowMapper;
  }

  
}
