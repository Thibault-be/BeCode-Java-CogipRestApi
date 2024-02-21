package org.thibault.cogiprestapi.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thibault.cogiprestapi.model.Company;

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
  
  private RowMapper<Company> getCompanyRowMapper(){
    
    RowMapper<Company> companyRowMapper = (resultSet, i ) -> {
      Company rowObject = new Company("","","","");
      rowObject.setId(resultSet.getInt("id"));
      rowObject.setName(resultSet.getString("name"));
      rowObject.setCountry(resultSet.getString("country"));
      rowObject.setVat(resultSet.getString("vat"));
      rowObject.setType("type");
      
      return rowObject;
    };
    return companyRowMapper;
  }

  
}
