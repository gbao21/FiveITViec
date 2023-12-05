package com.DATN.FiveITViec.CommonController.AdminController;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.UserStatusAndApprovalDTO;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.dto.UserManagementDTO;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.MailService;
import com.DATN.FiveITViec.services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController()
@RequestMapping("/auth/admin")
public class EmployerAdminManagementController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private MailService mailService;

    @GetMapping("/getAllEmployers")
    public ResponseEntity<?> getAllEmployer(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name="email") String email,
            @RequestParam(name="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name="status") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name="approval") String approval){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                if(approval.equals(FiveITConstants.APPROVAL_STATUS)){
                    Page<UserManagementDTO> getAllEmployer = userService.getAllUsersByRoles(FiveITConstants.EMPLOYER, email, FiveITConstants.APPROVAL_STATUS, startDate, endDate,status, PageRequest.of(page, size));
                    return new ResponseEntity<>(getAllEmployer, HttpStatus.OK);
                }
                if(approval.equals(FiveITConstants.WAITING_STATUS)){
                    Page<UserManagementDTO> getAllEmployer = userService.getAllUsersByRoles(FiveITConstants.EMPLOYER, email, FiveITConstants.WAITING_STATUS, startDate, endDate,status, PageRequest.of(page, size));
                    return new ResponseEntity<>(getAllEmployer, HttpStatus.OK);
                }

            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/updateEmployerStatus")
    public ResponseEntity<String> updateEmployerStatus(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader, @RequestBody UserStatusAndApprovalDTO userStatusAndApprovalDTO){
        System.out.println(userStatusAndApprovalDTO.toString());
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {

                Optional<User> user = userRepository.findById(userStatusAndApprovalDTO.getUserId());
                if(user.isPresent()){
                    user.get().setStatus(userStatusAndApprovalDTO.getStatus());
                    userRepository.save(user.get());
                    return new ResponseEntity<>("Updated", HttpStatus.OK);
                }

            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/updateEmployerApproval")
    public ResponseEntity<String> updateEmployerApproval(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader, @RequestBody UserStatusAndApprovalDTO userStatusAndApprovalDTO) throws MessagingException {
        System.out.println(userStatusAndApprovalDTO.toString());
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {

                Optional<User> user = userRepository.findById(userStatusAndApprovalDTO.getUserId());
                if(user.isPresent()){
                    user.get().setStatus(userStatusAndApprovalDTO.getStatus());
                    user.get().setApproval(userStatusAndApprovalDTO.getApproval());


                    String emailFrom = "nganvps24932@fpt.edu.vn";
                    mailService.sendEmailApprovalEmployer(user.get(), emailFrom);

                    userRepository.save(user.get());
                    return new ResponseEntity<>("Updated", HttpStatus.OK);
                }

            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/getTotalApprovedEmployerByMonth")
    public  ResponseEntity<?> getTotalApprovedEmployerByMonth(
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
                Map<Object, Object> totalApprovedEmployer = new HashMap<>();
                Map<Object, Object> responseMap = new HashMap<>();
                for(int i = 1;i<=12; i++){
                    long candidates = userRepository.countAllEmployerByMonthAndStatus(FiveITConstants.EMPLOYER,FiveITConstants.APPROVAL_STATUS,i,yearCurrent);
                    totalApprovedEmployer.put(i,candidates);

                }
                responseMap.put("approvedEmployerMap",totalApprovedEmployer);


                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/getTotalWaitingEmployerByMonth")
    public  ResponseEntity<?> getTotalWaitingEmployerByMonth(
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
                Map<Object, Object> totalWaitingEmployer = new HashMap<>();
                Map<Object, Object> responseMap = new HashMap<>();
                for(int i = 1;i<=12; i++){
                    long candidates = userRepository.countAllEmployerByMonthAndStatus(FiveITConstants.EMPLOYER,FiveITConstants.WAITING_STATUS,i,yearCurrent);
                    totalWaitingEmployer.put(i,candidates);

                }
                responseMap.put("waitingEmployerMap",totalWaitingEmployer);


                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }
}

