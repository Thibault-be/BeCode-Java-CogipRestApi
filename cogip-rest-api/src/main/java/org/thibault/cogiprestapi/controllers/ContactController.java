package org.thibault.cogiprestapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.thibault.cogiprestapi.model.Contact;
import org.thibault.cogiprestapi.services.ContactService;

import java.util.List;

@RestController
@RequestMapping
public class ContactController {
  
  private final ContactService contactService;
  
  public ContactController(ContactService contactService){
    this.contactService = contactService;
  }
  
  @GetMapping ("/contacts")
  public List<Contact> getAllContacts() {
    return this.contactService.getAllContacts();
  }
  
  @GetMapping ("/contacts/{id}")
  public Contact getContactById(@PathVariable("id") int id){
    return this.contactService.getContactById(id);
  }
  
  
  @GetMapping ("/contacts/search")
  public List<Contact> getContactsByFilters(
                          @RequestParam (required = false) Integer id,
                          @RequestParam (required = false) String firstname,
                          @RequestParam (required = false) String lastname,
                          @RequestParam (required = false) String phone,
                          @RequestParam (required = false) Integer companyId
  ){
    return this.contactService.getContactsByFilters(id, firstname, lastname, phone, companyId);
  }
  

  @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTANT')")
  @PostMapping ("/contacts")
  public ResponseEntity<Contact> addContact(@RequestBody Contact contact){
    Contact returnContact = this.contactService.addContact(contact);
    
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(returnContact);
  }
  
  @PutMapping ("/contacts/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTANT')")
  public ResponseEntity<Contact> updateContact(@PathVariable("id") int id, @RequestBody Contact contact){
    Contact updatedContact = this.contactService.updateContact(id, contact);
    
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedContact);
  }
  
  @DeleteMapping("/contacts/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTANT')")
  public ResponseEntity<String> deleteContact(@PathVariable int id){
    this.contactService.deleteContact(id);
    
    return ResponseEntity
            .status(HttpStatus.OK)
            .body("The contact with id " + id + " was successfully deleted.");
  }
}
