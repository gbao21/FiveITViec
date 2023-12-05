package com.DATN.FiveITViec.CommonController.AdminController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.model.ProfileAdmin;
import com.DATN.FiveITViec.model.Roles;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.repository.ProfileAdminRepository;
import com.DATN.FiveITViec.repository.RolesRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.security.JWTGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
@RequestMapping("/auth/admin")
public class AdminProfileController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private ProfileAdminRepository profileAdminRepository;

    @Autowired
    private JWTGenerator jwtGenerator;

    @PostMapping("/createNewAdmin")
    public ResponseEntity<String> createNewAdmin(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name = "name") String name, @RequestParam(name = "email") String email,
            @RequestParam(name = "phoneNumber") String phoneNumber, @RequestParam(name = "password") String password) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                User userByEmail = userRepository.findByEmail(email);
                if (userByEmail != null) {
                    System.out.println(userByEmail.toString());
                    return new ResponseEntity<>("Email exited", org.springframework.http.HttpStatus.BAD_REQUEST);
                }
                String emailToken = jwtGenerator.getUsernameFromJWT(token);
                User user = userRepository.findByEmail(emailToken);

                if (user != null) {
                    String role = user.getRoles().getRoleName();
                    if (role.equals("admin")) {
                        User u = new User();
                        Roles roles = rolesRepository.getByRoleName("admin");
                        u.setEmail(email);
                        u.setPassword(passwordEncoder.encode(password));
                        u.setRoles(roles);
                        u.setStatus(FiveITConstants.ENABLE_STATUS);
                        u.setApproval(FiveITConstants.APPROVAL_STATUS);
                        userRepository.save(u);

                        ProfileAdmin profileAdmin = new ProfileAdmin();
                        profileAdmin.setUser(u);
                        profileAdmin.setPhoneNumber(phoneNumber);
                        profileAdmin.setName(name);

                        profileAdminRepository.save(profileAdmin);
                        return new ResponseEntity<>("Admin has been created", org.springframework.http.HttpStatus.OK);
                    }
                }
            }
        }
        return new ResponseEntity<>(null, org.springframework.http.HttpStatus.UNAUTHORIZED);
    }
}
