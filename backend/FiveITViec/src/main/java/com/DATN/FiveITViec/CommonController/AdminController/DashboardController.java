package com.DATN.FiveITViec.CommonController.AdminController;


import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.UserProfileDTO;
import com.DATN.FiveITViec.model.Contact;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.repository.*;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RestController()
@RequestMapping("/auth/admin")
public class DashboardController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private ApplicantRepository applicantRepository;

    @GetMapping("/totalAdminPage")
    public  ResponseEntity<?> getAllTotalAdminPage(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                long totalCandidate = userRepository.countUserByRole(FiveITConstants.CANDIDATE);
                long totalEmployer = userRepository.countUserByRole(FiveITConstants.EMPLOYER);
                long totalJob = jobRepository.countAllJob();
                long totalBlog = blogRepository.countAllBlog();
                long totalContact = contactRepository.countAllContact();
                Map<String, String> total = new HashMap<>();
                total.put("totalCandidate", String.valueOf(totalCandidate));
                total.put("totalEmployer", String.valueOf(totalEmployer));
                total.put("totalJob", String.valueOf(totalJob));
                total.put("totalBlog", String.valueOf(totalBlog));
                total.put("totalContact", String.valueOf(totalContact));
                return new ResponseEntity<>(total, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("/getQuantityUserByMonth")
    public  ResponseEntity<?> getQuantityUserByMonth(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name = "yearUser",required = false) String yearUser
            ,@RequestParam(name = "yearJob",required = false) String yearJob){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                int yearUserCurrent = 0;
                int yearJobCurrent = 0;
                if(yearUser!=null){
                    yearUserCurrent= Integer.valueOf(yearUser);
                }else{
                    yearUserCurrent = YearMonth.now().getYear();
                }

                if(yearJob!=null){
                    yearJobCurrent= Integer.valueOf(yearJob);
                }else{
                    yearJobCurrent = YearMonth.now().getYear();
                }
                Map<Object, Object> totalCandidate = new HashMap<>();
                Map<Object, Object> totalEmployer = new HashMap<>();
                Map<Object, Object> totalApplicant = new HashMap<>();
                Map<Object, Object> totalJob = new HashMap<>();
                Map<String, Object> responseMap = new HashMap<>();
                for(int i = 1;i<=12; i++){
                    long candidates = userRepository.countAllCandidatesByMonth(FiveITConstants.CANDIDATE,i,yearUserCurrent);
                    totalCandidate.put(i,candidates);
                    long employers = userRepository.countAllCandidatesByMonth(FiveITConstants.EMPLOYER,i,yearUserCurrent);
                    totalEmployer.put(i,employers);
                    long applicants = applicantRepository.countAllApplicantByMonth(i,yearJobCurrent);
                    totalApplicant.put(i,applicants);
                    long jobs = jobRepository.countAllJobByMonth(i,yearJobCurrent);
                    totalJob.put(i,jobs);
                }
                responseMap.put("candidateMap",totalCandidate);
                responseMap.put("employerMap",totalEmployer);
                responseMap.put("applicantMap",totalApplicant);
                responseMap.put("jobMap",totalJob);


                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/lastedUser")
    public  ResponseEntity<?> lastedUser(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                List<UserProfileDTO> listUser = userService.getLastedEmployer();
                return new ResponseEntity<>(listUser, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }
}