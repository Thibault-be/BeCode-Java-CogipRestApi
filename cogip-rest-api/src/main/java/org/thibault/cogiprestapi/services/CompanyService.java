package org.thibault.cogiprestapi.services;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.stereotype.Service;
import org.thibault.cogiprestapi.enums.CompanyType;
import org.thibault.cogiprestapi.exceptions.IdNotFoundException;
import org.thibault.cogiprestapi.exceptions.IllegalParametersException;
import org.thibault.cogiprestapi.exceptions.ParametersMissingException;
import org.thibault.cogiprestapi.exceptions.ResultSetEmptyException;
import org.thibault.cogiprestapi.model.Company;
import org.thibault.cogiprestapi.repositories.CompanyRepository;

import java.util.List;

@Service
public class CompanyService {
  
  private final CompanyRepository companyRepository;
  
  public CompanyService(CompanyRepository companyRepository){
    this.companyRepository = companyRepository;
  }
  
  public List<Company> getAllCompanies(){
    List<Company> allCompanies = this.companyRepository.getAllCompanies();
    if (allCompanies.isEmpty()) throw new ResultSetEmptyException("No companies were found.");
    return allCompanies;
  }
  
  public Company getCompanyById(int id){
    try {
      return this.companyRepository.getCompanyById(id);
    } catch (EmptyResultDataAccessException e){
      throw new IdNotFoundException("Company with id " + id + " does not exist");
    }
  }
  
  public List<Company> searchCompaniesByFilters(Integer id, String name, String country, String vat, CompanyType type){
    
    try{
      getCompanyById(id);
    } catch (EmptyResultDataAccessException ex){
      throw new IdNotFoundException("Company with id " + id + " does not exist.");
    }
    
    List<Company> filteredCompanies = this.companyRepository.searchCompaniesByFilters(id, name, country, vat, type);
    if (filteredCompanies.isEmpty()) throw new ResultSetEmptyException("No companies for your filters were found.");
    return filteredCompanies;
  }
  
  public void addCompany(Company company){
    System.out.println("not even here");
    String missingParams = missingParameters(company);
    if (missingParams != null) throw new ParametersMissingException(missingParams);
    
    System.out.println(correctEnum(company.getType()));
    
    try{
      System.out.println("test");
      correctEnum(company.getType());
      System.out.println("nog");
    } catch (Exception e){
      System.out.println(e.getClass());
      throw new IllegalParametersException("Company type should be CLIENT or PROVIDER.");
    }
    
    //this.companyRepository.addCompany(company);
  }
  
  public void deleteCompany(int id){
    this.companyRepository.deleteCompany(id);
  }
  
  public Company updateCompany(int id, Company company){
    return this.companyRepository.updateCompany(id, company);
  }
  
  
  private String missingParameters(Company company){
    StringBuilder params = new StringBuilder();
    System.out.println("in params");
    params.append("These are the missing parameters:\n");
    boolean flag = false;
    if (company.getName() == null || company.getName().isEmpty()){
      params.append("company name\n");
      flag = true;
    }
    if (company.getVat() == null || company.getVat().isEmpty()){
      params.append("vat");
      flag = true;
    }
    if (company.getType() == null){
      params.append("type");
      flag = true;
    }
    if (flag) return params.toString();
    return null;
  }
  
  private boolean correctEnum(CompanyType type){
    System.out.println("i am here");
    if (type.equals(CompanyType.CLIENT)) return true;
    if (type.equals(CompanyType.PROVIDER)) return true;
    return false;
  }
  
}
