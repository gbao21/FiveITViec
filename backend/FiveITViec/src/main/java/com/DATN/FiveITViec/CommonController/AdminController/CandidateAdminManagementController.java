package com.DATN.FiveITViec.CommonController.AdminController;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.UserStatusAndApprovalDTO;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.dto.UserManagementDTO;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.UserService;
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
public class CandidateAdminManagementController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTGenerator jwtGenerator;

    @GetMapping("/getAllCandidates")
    public ResponseEntity<?> getAllCandidates(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name="email") String email,
            @RequestParam(name="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name="status") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size){

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                Page<UserManagementDTO> getAllCandidates = userService.getAllUsersByRoles(FiveITConstants.CANDIDATE, email, FiveITConstants.APPROVAL_STATUS,startDate, endDate,status,PageRequest.of(page, size));
                return new ResponseEntity<>(getAllCandidates, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }
    @PutMapping("/updateCandidateStatus")
    public ResponseEntity<String> updateCandidateStatus(
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

    @GetMapping("/getTotalCandidateByMonth")
    public  ResponseEntity<?> getTotalCandidateByMonth(
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
                Map<Object, Object> totalCandidate = new HashMap<>();
                Map<Object, Object> responseMap = new HashMap<>();
                for(int i = 1;i<=12; i++){
                    long candidates = userRepository.countAllCandidatesByMonth(FiveITConstants.CANDIDATE,i,yearCurrent);
                    totalCandidate.put(i,candidates);

                }
                responseMap.put("candidateMap",totalCandidate);


                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }

}
