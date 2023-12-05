package com.DATN.FiveITViec.CommonController;

import com.DATN.FiveITViec.repository.ProfileCandidateRepository;
import com.DATN.FiveITViec.repository.ProfileEmployerRepository;
import com.DATN.FiveITViec.repository.RolesRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.*;
import com.DATN.FiveITViec.model.*;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.MailService;
import com.DATN.FiveITViec.services.UserService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000/")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private ProfileEmployerRepository  profileEmployerRepository;
    @Autowired
    private ProfileCandidateRepository profileCandidateRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private MailService mailService;

    @PostMapping("/register")
    public ResponseEntity<String> responseEntityRegister (@RequestBody RegisterDTO registerDTO){

        if(userService.existUser(registerDTO.getEmail())){
            return new ResponseEntity<>("Email has already existed", HttpStatus.BAD_REQUEST);
        }
        System.out.println(registerDTO.toString());
        String defaultImg = "https://res.cloudinary.com/dzqoi9laq/image/upload/v1699419757/logoo_pyz2sp.png";

        if(registerDTO.getRegisterType().equals("candidate")){
            User user = new User();
            Roles roles = rolesRepository.getByRoleName("candidate");
            user.setEmail(registerDTO.getEmail());
            user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            user.setRoles(roles);
            user.setStatus(FiveITConstants.ENABLE_STATUS);
            user.setApproval(FiveITConstants.APPROVAL_STATUS);
            userRepository.save(user);

            ProfileCandidate profileCandidate = new ProfileCandidate();
            profileCandidate.setUser(user);
            profileCandidate.setPhoneNumber(registerDTO.getPhoneNumber());
            profileCandidate.setUserImage(defaultImg);
            profileCandidate.setName(registerDTO.getName());

            profileCandidateRepository.save(profileCandidate);
            return new ResponseEntity<>("User has been created", HttpStatus.OK);

        }
        if(registerDTO.getRegisterType().equals("employer")){
            User user = new User();
            Roles roles = rolesRepository.getByRoleName("employer");
            user.setEmail(registerDTO.getEmail());
            user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            user.setRoles(roles);
            user.setStatus(FiveITConstants.DISABLE_STATUS);
            user.setApproval(FiveITConstants.WAITING_STATUS);
            userRepository.save(user);

            ProfileEmployer profileEmployer = new ProfileEmployer();
            profileEmployer.setUser(user);
            profileEmployer.setName(registerDTO.getName());
            profileEmployer.setAddress(registerDTO.getAddress());
            profileEmployer.setCompanyName(registerDTO.getCompanyName());
            profileEmployer.setTaxNumber(registerDTO.getTaxNumber());
            profileEmployer.setBio(registerDTO.getBio());
            profileEmployer.setCompanyLogo(defaultImg);
            profileEmployer.setPhoneNumber(registerDTO.getPhoneNumber());
            profileEmployerRepository.save(profileEmployer);

            return new ResponseEntity<>("Employer has been created", HttpStatus.OK);
        }
        return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/login")
    public ResponseEntity<?> responseEntityLogin (@RequestBody LoginDTO loginDTO){
        String checkLogin  = userService.checkLogin(loginDTO);
        ErrorLoginDTO errorLoginDTO = new ErrorLoginDTO();
        if(checkLogin.equals(FiveITConstants.DISABLE_STATUS)){
            errorLoginDTO.setError(FiveITConstants.DISABLE_STATUS);
            return new ResponseEntity<>(errorLoginDTO,HttpStatus.BAD_REQUEST);
        }
        if(checkLogin.equals(FiveITConstants.NO_EMAIL)){
            errorLoginDTO.setError(FiveITConstants.NO_EMAIL);
            return new ResponseEntity<>(errorLoginDTO,HttpStatus.BAD_REQUEST);
        }
        if(checkLogin.equals(FiveITConstants.NO_PASSWORD)){
            errorLoginDTO.setError(FiveITConstants.NO_PASSWORD);
            return new ResponseEntity<>(errorLoginDTO,HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }
    @PostMapping("/loginWithGoogleAndFacebook")
	public ResponseEntity<?> responseEntityLoginWithGoogle(@RequestBody Map<String, String> googleUserData)
			throws MessagingException {
		String email = googleUserData.get("email");
		User user = userRepository.findByEmail(email);
		if (user == null) {
			user = createNewUser(googleUserData);
		}
		String token = jwtGenerator.generateTokenForDifferentLogin(user);
		return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
	}

	private User createNewUser(Map<String, String> googleUserData) throws MessagingException {
		String email = googleUserData.get("email");
		String name = googleUserData.get("name");
		String picture = googleUserData.get("picture");

		Roles roles = rolesRepository.getByRoleName("candidate");
		User newUser = new User();
        String passwordGenerated = userService.generateRandomPassword();
		newUser.setEmail(email);
		newUser.setPassword(passwordEncoder.encode(passwordGenerated)); // Temporary password
		newUser.setRoles(roles);
		newUser.setStatus(FiveITConstants.ENABLE_STATUS);
		newUser.setApproval(FiveITConstants.APPROVAL_STATUS);
		userRepository.save(newUser);

		// Send email to notice User
		String emailFrom = "nganvps24932@fpt.edu.vn";
		String emailTo = email;

		mailService.sendEmailNoticeUserLoginWithDifferentWays(passwordGenerated, emailFrom, emailTo);

		ProfileCandidate profileCandidate = new ProfileCandidate();
		profileCandidate.setUser(newUser);
		profileCandidate.setPhoneNumber("0123456789");
		profileCandidate.setUserImage(picture);
		profileCandidate.setName(name);
		profileCandidateRepository.save(profileCandidate);
		return newUser;
	}

    @GetMapping("/logout")
    public ResponseEntity<String> responseEntityLogout(){
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>("Logout successfully ", HttpStatus.OK);
    }


    @PostMapping("/resetPassword")
    public ResponseEntity<String> responseEntityResetPassword(@RequestParam("email") String email) throws MessagingException {
        User user = userRepository.findByEmail(email);
        if(user == null){
            return new ResponseEntity<>("Email doesn't exist ", HttpStatus.BAD_REQUEST);
        }
        if(userService.updatePassword(user) != null){
            String emailFrom = "nganvps24932@fpt.edu.vn";
            String emailTo = email;
            String newPass = userService.updatePassword(user);
            mailService.sendEmailResetPassword(user,newPass,  emailFrom,emailTo);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Errors", HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping("/getUserId")
	public ResponseEntity<?> getUserId(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				String email = jwtGenerator.getUsernameFromJWT(token);
				User user = userRepository.findByEmail(email);
				if (user != null) {
					String role = user.getRoles().getRoleName();
						return new ResponseEntity<>(user.getUserId(), HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
	}


}
