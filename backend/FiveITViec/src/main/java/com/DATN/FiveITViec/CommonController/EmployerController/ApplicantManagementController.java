package com.DATN.FiveITViec.CommonController.EmployerController;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.model.Applicant;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.repository.ApplicantRepository;
import com.DATN.FiveITViec.repository.JobRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.ApplicantService;
import com.DATN.FiveITViec.services.UserService;

@RestController
@RequestMapping("/auth/employer")
public class ApplicantManagementController {
	@Autowired
	private JWTGenerator jwtGenerator;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private ApplicantService applicantService;
	@Autowired
	private ApplicantRepository applicantRepository;

	@PutMapping("/approvedApplicant")
	public ResponseEntity<?> deniedApplicant(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam("applicantId") Long applicantId, @RequestParam("message") String message,
			@RequestParam("status") String status) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				String email = jwtGenerator.getUsernameFromJWT(token);
				User user = userRepository.findByEmail(email);
				Optional<Applicant> applicant = applicantRepository.findById(applicantId);
				if (user.getRoles().getRoleName().equals(FiveITConstants.EMPLOYER) && applicant.get() != null
						&& applicant.get().getStatus().equals(FiveITConstants.WAITING_STATUS)) {
					if (status.equals(FiveITConstants.APPROVAL_STATUS)) {
						Applicant applicantApproved = applicant.get();
						applicantApproved.setStatus(FiveITConstants.APPROVAL_STATUS);
						applicantApproved.setMessage(message);
						applicantRepository.save(applicantApproved);
						return new ResponseEntity<>("Approved Applicant Successful", HttpStatus.OK);
					}

					if (status.equals(FiveITConstants.CLOSE_STATUS)) {
						Applicant applicantClose = applicant.get();
						applicantClose.setStatus(FiveITConstants.CLOSE_STATUS);
						applicantClose.setMessage(message);
						applicantRepository.save(applicantClose);
						return new ResponseEntity<>("Close Applicant successful", HttpStatus.OK);
					}

				} else {
					return new ResponseEntity<>("Close Applicant Fail ", HttpStatus.BAD_REQUEST);
				}

			}

		}
		return new ResponseEntity<>("Fail", HttpStatus.UNAUTHORIZED);
	}
}
