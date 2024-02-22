package org.thibault.cogiprestapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public List<Contact> getAllContact() {
    return this.contactService.getAllContacts();
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
  
  @PostMapping ("/contacts/add")
  public ResponseEntity<String> addContact(@RequestBody Contact contact){
    this.contactService.addContact(contact);
    
    return ResponseEntity
            .status(HttpStatus.OK)
            .body("Contact " + contact.getFirstname() + " " + contact.getLastname() + " was successfully added.");
  }
  
  @PutMapping ("/contacts/update")
  public ResponseEntity<Contact> updateContact(@RequestBody Contact contact){
    Contact updatedContact = this.contactService.updateContact(contact);
    
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedContact);
  }
  
  @DeleteMapping("/contacts/delete/{id}")
  public ResponseEntity<String> deleteContact(@PathVariable int id){
    this.contactService.deleteContact(id);
    
    return ResponseEntity
            .status(HttpStatus.OK)
            .body("The contact with id " + id + " was successfully deleted.");
  }
}
