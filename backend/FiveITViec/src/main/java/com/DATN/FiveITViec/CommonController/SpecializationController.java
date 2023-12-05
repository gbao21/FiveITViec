package com.DATN.FiveITViec.CommonController;

import com.DATN.FiveITViec.repository.SpecializationRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.SpecializationService;
import com.DATN.FiveITViec.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class SpecializationController {
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    SpecializationRepository specializationRepository;
    @Autowired
    SpecializationService specializationService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

//    @DeleteMapping("/removeSpecialization")
//    public ResponseEntity<String> removeSpecialization(@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
//                                                       @RequestParam("speId") Long speId) {
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//            if (jwtGenerator.validateToken(token)) {
//                String email = jwtGenerator.getUsernameFromJWT(token);
//                User user = userRepository.findByEmail(email);
////                if(user.getRoles().getRoleName().equals("admin")){
////
////                }
//                if (speId != null) {
//                    Specialization spe = specializationRepository.findBySpecializationId(speId);
//                    System.out.println(spe);
//                        specializationRepository.delete(spe);
//                    return new ResponseEntity<>("Completed", HttpStatus.OK);
//
//                    }
//                }
//
//            }
//        return new ResponseEntity<>("Delete failed", HttpStatus.BAD_REQUEST);
//
//        }

    }
