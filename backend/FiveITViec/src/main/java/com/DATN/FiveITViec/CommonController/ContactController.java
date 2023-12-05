package com.DATN.FiveITViec.CommonController;

import com.DATN.FiveITViec.repository.ContactRepository;
import com.DATN.FiveITViec.dto.ContactDTO;
import com.DATN.FiveITViec.model.Contact;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ContactController {
    @Autowired
    public ContactService contactService;

    @Autowired
    public ContactRepository contactRepository;

    @Autowired
    public JWTGenerator jwtGenerator;

    @PostMapping("/contact")
    public ResponseEntity<String> responseEntity ( @RequestHeader(name = "Authorization", required = false) String authorizationHeader, @RequestBody ContactDTO contactDTO){
        Contact contact = new Contact();
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token  = authorizationHeader.substring(7);
            if(jwtGenerator.validateToken(token)){
                contact.setName(contactDTO.getName());
                contact.setEmail(contactDTO.getEmail());
                contact.setSubject(contactDTO.getSubject());
                contact.setMessage(contactDTO.getMessage());
                contact.setStatus(contactDTO.getStatus());

            }else{
                contact.setName(contactDTO.getName());
                contact.setEmail(contactDTO.getEmail());
                contact.setSubject(contactDTO.getSubject());
                contact.setMessage(contactDTO.getMessage());
                contact.setStatus(contactDTO.getStatus());
            }
            contactRepository.save(contact);
        }
        return new ResponseEntity<>("Save Contact successfully", HttpStatus.OK);
    }



}
