package com.DATN.FiveITViec.services;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.ContactDTO;
import com.DATN.FiveITViec.dto.UserManagementDTO;
import com.DATN.FiveITViec.model.*;
import com.DATN.FiveITViec.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;


    public boolean updateMsgStatus(int contactUsId, String status) {
        boolean isUpdated = false;
        long result = contactRepository.updateByStatus(status, contactUsId);
        if(result > 0 ) {
            isUpdated = true;
        }
        return isUpdated;
    }


    public Page<ContactDTO> getAllContactByEmailAndStatus(String email, String status, Pageable pageable) {
        Page<Contact> pageOfContacts = contactRepository.findByEmailAndStatus(email, status, pageable);

        List<ContactDTO> listContactDTO = new ArrayList<>();

        for (Contact contact : pageOfContacts.getContent()) {
            ContactDTO contactDTO = new ContactDTO();
            contactDTO.setContactId(contact.getContactId());
            contactDTO.setName(contact.getName());
            contactDTO.setEmail(contact.getEmail());
            contactDTO.setSubject(contact.getSubject());
            contactDTO.setMessage(contact.getMessage());
            contactDTO.setCreatedAt(contact.getCreatedAt());
            contactDTO.setCreatedBy(contact.getCreatedBy());
            contactDTO.setUpdatedAt(contact.getUpdatedAt());
            contactDTO.setUpdatedBy(contact.getUpdatedBy());
            contactDTO.setStatus(contact.getStatus());

            listContactDTO.add(contactDTO);
        }
        return new PageImpl<>(listContactDTO, pageable, pageOfContacts.getTotalElements());
    }
}
