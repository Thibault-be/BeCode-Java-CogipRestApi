package org.thibault.cogiprestapi.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.thibault.cogiprestapi.exceptions.IdNotFoundException;
import org.thibault.cogiprestapi.exceptions.IllegalParametersException;
import org.thibault.cogiprestapi.exceptions.ParametersMissingException;
import org.thibault.cogiprestapi.exceptions.ResultSetEmptyException;
import org.thibault.cogiprestapi.model.Contact;
import org.thibault.cogiprestapi.repositories.ContactRepository;

import java.util.List;

@Service
public class ContactService {
  
  private final ContactRepository contactRepository;
  
  public ContactService(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }
  
  public List<Contact> getAllContacts(){
    List<Contact> allContacts = this.contactRepository.getAllContacts();
    if (allContacts.isEmpty()) throw new ResultSetEmptyException("There were no contacts found.");
    return allContacts;
  }
  
  public Contact getContactById(int id){
    try {
      return this.contactRepository.getContactById(id);
    } catch (EmptyResultDataAccessException ex){
      throw new IdNotFoundException("The contact with id " + id + " does not exist.");
    }
  }
  
  public List<Contact> getContactsByFilters(Integer id, String firstname, String lastname, String phone, Integer companyId){
    List<Contact> filteredContacts = this.contactRepository.getContactsByFilters(id, firstname, lastname, phone, companyId);
    if (filteredContacts.isEmpty()) {
      throw new ResultSetEmptyException("No contacts found for the selected filters.");
    }
    return filteredContacts;
  }
  
  public Contact addContact(Contact contact){
    String allFields = parametersMissing(contact);
    System.out.println(allFields);
    if (allFields != null) throw new ParametersMissingException(allFields);
    
    return this.contactRepository.addContact(contact);
  }
  
  public Contact updateContact(int id, Contact contact){
    try {
      return this.contactRepository.updateContact(id, contact);
    } catch (EmptyResultDataAccessException mtre){
      throw new IdNotFoundException("Contact with id "+id+" does not exist.");
    } catch (DataIntegrityViolationException dive){
      throw new IllegalParametersException("The company id that you've entered does not exist.");
    }
  }
  
  public void deleteContact(int id){
    try{
      this.contactRepository.deleteContact(id);
    } catch (EmptyResultDataAccessException ex){
      throw new IdNotFoundException("Contact with id " + id + " does not exist.");
    }
  }
  
  private String parametersMissing(Contact contact){
    StringBuilder params = new StringBuilder();
    params.append("These are the missing parameters:\n");

    boolean flag = false;

    if (contact.getFirstname() == null){
      params.append("firstname\n");
      flag = true;
    }

    if (contact.getLastname() == null){
      params.append("lastname\n");
      flag = true;
    }
    
    System.out.println(contact.getCompanyId());
    if (contact.getCompanyId() == null || contact.getCompanyId() == 0){
      params.append("company_id");
      flag = true;
    }
    
    if (flag) return params.toString();
    return null;
  }
}
