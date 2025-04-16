package io.github.muriloaugusto1.hubspotintegration.controller;

import io.github.muriloaugusto1.hubspotintegration.model.ContactRequest;
import io.github.muriloaugusto1.hubspotintegration.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<?> createContact(@RequestBody ContactRequest contactRequest) {
        return ResponseEntity.ok(contactService.createContact(contactRequest).block());
    }
}
