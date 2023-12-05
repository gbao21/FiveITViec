package com.DATN.FiveITViec.CommonController.AdminController;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.ContactDTO;
import com.DATN.FiveITViec.dto.ContactStatusDTO;
import com.DATN.FiveITViec.dto.UserManagementDTO;
import com.DATN.FiveITViec.dto.UserStatusAndApprovalDTO;
import com.DATN.FiveITViec.model.Contact;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.repository.ContactRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.ContactService;
import com.DATN.FiveITViec.services.MailService;
import com.DATN.FiveITViec.services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController()
@RequestMapping("/auth/admin")
public class ContactManagementController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private MailService mailService;

    @GetMapping("/getAllContacts")
    public ResponseEntity<?> getAllContacts(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name="email") String email,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size, @RequestParam(name="status") String status){

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                if(status.equals(FiveITConstants.OPEN_STATUS)){
                Page<ContactDTO> getAllOpenContact = contactService.getAllContactByEmailAndStatus( email, FiveITConstants.OPEN_STATUS, PageRequest.of(page, size));
                    return new ResponseEntity<>(getAllOpenContact, HttpStatus.OK);
                }
                if(status.equals(FiveITConstants.CLOSE_STATUS)){
                    Page<ContactDTO> getAllCloseContact = contactService.getAllContactByEmailAndStatus( email, FiveITConstants.CLOSE_STATUS, PageRequest.of(page, size));
                    return new ResponseEntity<>(getAllCloseContact, HttpStatus.OK);
                }

            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/updateContactStatus")
    public ResponseEntity<String> updateContactStatus(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader, @RequestBody ContactStatusDTO contactStatusDTO){
        System.out.println(contactStatusDTO.toString());
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {

                Optional<Contact> contact = contactRepository.findById(contactStatusDTO.getContactId());
                if(contact.isPresent()){
                    contact.get().setStatus(contactStatusDTO.getStatus());
                    contactRepository.save(contact.get());
                    return new ResponseEntity<>("Updated", HttpStatus.OK);
                }

            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("/getTotalCloseContactByMonth")
    public  ResponseEntity<?> getTotalCloseContactByMonth(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name = "year",required = false) String year){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                int yearCurrent = 0;
                if(year!=null){
                    yearCurrent= Integer.valueOf(year);
                }else{
                    yearCurrent = YearMonth.now().getYear();
                }
                Map<Object, Object> totalApprovedContact= new HashMap<>();
                Map<Object, Object> responseMap = new HashMap<>();
                for(int i = 1;i<=12; i++){
                    long contact = contactRepository.countAllContactByMonthAndStatus(FiveITConstants.CLOSE_STATUS,i,yearCurrent);
                    totalApprovedContact.put(i,contact);

                }
                responseMap.put("closeContactMap",totalApprovedContact);


                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/getTotalOpenContactByMonth")
    public  ResponseEntity<?> getTotalOpenContactByMonth(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name = "year",required = false) String year){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                int yearCurrent = 0;
                if(year!=null){
                    yearCurrent= Integer.valueOf(year);
                }else{
                    yearCurrent = YearMonth.now().getYear();
                }
                Map<Object, Object> totalWaitingContact = new HashMap<>();
                Map<Object, Object> responseMap = new HashMap<>();
                for(int i = 1;i<=12; i++){
                    long contact = contactRepository.countAllContactByMonthAndStatus(FiveITConstants.OPEN_STATUS,i,yearCurrent);
                    totalWaitingContact.put(i,contact);

                }
                responseMap.put("openContactMap",totalWaitingContact);


                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }
}

