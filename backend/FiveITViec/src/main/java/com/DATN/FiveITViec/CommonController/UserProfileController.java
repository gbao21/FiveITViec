package com.DATN.FiveITViec.CommonController;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.DATN.FiveITViec.model.*;
import com.DATN.FiveITViec.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.AuthResponseDTO;
import com.DATN.FiveITViec.dto.ChangingPasswordDTO;
import com.DATN.FiveITViec.dto.UserProfileDTO;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.FileUploadService;
import com.DATN.FiveITViec.services.MailService;
import com.DATN.FiveITViec.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth/profile")
public class UserProfileController {
	@Autowired
	private JWTGenerator jwtGenerator;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	public ProfileCandidateRepository profileCandidateRepository;
	@Autowired
	public ProfileEmployerRepository profileEmployerRepository;

	@Autowired
	public ProfileAdminRepository profileAdminRepository;

	@Autowired
	public SpecializationRepository specializationRepository;

	@Autowired
	public FileUploadService fileUploadService;

	@Autowired
	public RolesRepository rolesRepository;

	@Autowired
	public PasswordEncoder passwordEncoder;

	@Autowired
	public MailService mailService;

	@GetMapping("/loadProfile")
	public ResponseEntity<UserProfileDTO> responseEntity(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				String email = jwtGenerator.getUsernameFromJWT(token);
				User user = userRepository.findByEmail(email);
				if (user != null) {
					String role = user.getRoles().getRoleName();
					Set<Specialization> specializations = user.getSpecializations();
					Set<Job> favoriteJob = user.getJobs();
					if (role.equals("candidate")) {
						ProfileCandidate profileCandidate = profileCandidateRepository
								.findProfileCandidateByUserId(user.getUserId());
						if (profileCandidate != null) {
							UserProfileDTO response = new UserProfileDTO(profileCandidate, specializations, favoriteJob);
							return new ResponseEntity<>(response, HttpStatus.OK);
						}

					} else if (role.equals("employer")) {
						ProfileEmployer profileEmployer = profileEmployerRepository
								.findProfileEmployerByUserId(user.getUserId());
						UserProfileDTO response = new UserProfileDTO(profileEmployer, specializations);
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else if (role.equals("admin")) {
						ProfileAdmin profileAdmin = profileAdminRepository.findProfileAdminByUserId(user.getUserId());
						UserProfileDTO response = new UserProfileDTO(profileAdmin);
						return new ResponseEntity<>(response, HttpStatus.OK);
					}


				}
			}
		}
		return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
	}

	@PostMapping("/uploadAvtProfile")
	public ResponseEntity<String> uploadFileImg(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam("file") MultipartFile multipartFile) throws IOException {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				String email = jwtGenerator.getUsernameFromJWT(token);
				User user = userRepository.findByEmail(email);

				String fileURL = fileUploadService.uploadFile(multipartFile);
				String userRole = user.getRoles().getRoleName();
				if (userRole.equals("candidate")) {
					ProfileCandidate profileCandidate = profileCandidateRepository
							.findProfileCandidateByUserId(user.getUserId());
					if (isImageFile(multipartFile)) {
						profileCandidate.setUserImage(fileURL);
						profileCandidateRepository.save(profileCandidate);
						return new ResponseEntity<>(profileCandidate.getUserImage(), HttpStatus.OK);
					}
				}
				if (userRole.equals("employer")) {
					ProfileEmployer profileEmployer = profileEmployerRepository
							.findProfileEmployerByUserId(user.getUserId());
					if (isImageFile(multipartFile)) {
						profileEmployer.setCompanyLogo(fileURL);
						profileEmployerRepository.save(profileEmployer);
						return new ResponseEntity<>(profileEmployer.getCompanyLogo(), HttpStatus.OK);
					}
				}
			}
		}

		return new ResponseEntity<>("Upload Fail", HttpStatus.BAD_REQUEST);
	}

	
	@PostMapping("/uploadCompanyImg")
	public ResponseEntity<String> uploadCompanyImg(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam("file") MultipartFile multipartFile, @RequestParam("imageIndex") int imageIndex) throws IOException {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				String email = jwtGenerator.getUsernameFromJWT(token);
				User user = userRepository.findByEmail(email);
				String fileURL = fileUploadService.uploadFile(multipartFile);
				String userRole = user.getRoles().getRoleName();
				if (userRole.equals("employer")) {
					ProfileEmployer profileEmployer = profileEmployerRepository
							.findProfileEmployerByUserId(user.getUserId());
					if (isImageFile(multipartFile)) {
						switch (imageIndex) {
							case 0:
								profileEmployer.setCompanyImg1(fileURL);
								profileEmployerRepository.save(profileEmployer);
								return new ResponseEntity<>(profileEmployer.getCompanyImg1(), HttpStatus.OK);
							case 1:
								profileEmployer.setCompanyImg2(fileURL);
								profileEmployerRepository.save(profileEmployer);
								return new ResponseEntity<>(profileEmployer.getCompanyImg2(), HttpStatus.OK);
							case 2:
								profileEmployer.setCompanyImg3(fileURL);
								profileEmployerRepository.save(profileEmployer);
								return new ResponseEntity<>(profileEmployer.getCompanyImg3(), HttpStatus.OK);
							default:
								break;
						}
						return new ResponseEntity<>(fileURL, HttpStatus.OK);
					}
				}
			}
		}
		return new ResponseEntity<>("Upload Fail", HttpStatus.BAD_REQUEST);
	}
	@PostMapping("/uploadCvProfile")
	public ResponseEntity<String> uploadFilePDF(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam("file") MultipartFile multipartFile) throws IOException {
		System.out.println(multipartFile);
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				String email = jwtGenerator.getUsernameFromJWT(token);
				User user = userRepository.findByEmail(email);

				String fileURL = fileUploadService.uploadFile(multipartFile);

				String userRole = user.getRoles().getRoleName();
				if (userRole.equals("candidate")) {
					ProfileCandidate profileCandidate = profileCandidateRepository
							.findProfileCandidateByUserId(user.getUserId());
					if (isPdfFile(multipartFile)) {
						profileCandidate.setUserCv(fileURL);
						profileCandidateRepository.save(profileCandidate);
						return new ResponseEntity<>(profileCandidate.getUserCv(), HttpStatus.OK);
					}
				}
			}
		}

		return new ResponseEntity<>("Upload Fail", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> responseEntityChangingPassword(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestBody ChangingPasswordDTO changingPasswordDTO) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				String email = jwtGenerator.getUsernameFromJWT(token);
				User user = userRepository.findByEmail(email);
				if (user != null) {
					if (userService.checkPasswordThenChange(changingPasswordDTO.getCurrentPassword(),
							changingPasswordDTO.getNewPassword(), user)) {
						return new ResponseEntity<>("Successfully change the password", HttpStatus.OK);
					} else {
						return new ResponseEntity<>("Wrong current password", HttpStatus.BAD_REQUEST);
					}
				}
			}
			return new ResponseEntity<>("Invalid Token", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Unauthorized", HttpStatus.BAD_REQUEST);
	}

	

	// Check if the uploaded file is an image (you may need to refine this based on
	// your needs)
	private boolean isImageFile(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType != null && contentType.startsWith("image/");
	}

	// Check if the uploaded file is a PDF (you may need to refine this based on
	// your needs)
	private boolean isPdfFile(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType != null && contentType.equals("application/pdf");
	}

	@PutMapping("/updateProfile")
	public ResponseEntity<String> updateProfile(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestBody UserProfileDTO userProfileDTO) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			System.out.println("Profile token: " + authorizationHeader);
			String token = authorizationHeader.substring(7);

			if (jwtGenerator.validateToken(token)) {
				String email = jwtGenerator.getUsernameFromJWT(token);
				User user = userRepository.findByEmail(email);
				if (user.getRoles().getRoleName().equals("candidate")) {
					ProfileCandidate profileUpdate = profileCandidateRepository
							.findProfileCandidateByUserId(user.getUserId());
					profileUpdate.setName(userProfileDTO.getName());
					profileUpdate.setGender(userProfileDTO.getGender());
					profileUpdate.setPhoneNumber(userProfileDTO.getPhoneNumber());
					profileUpdate.setAddress(userProfileDTO.getAddress());
					profileUpdate.setBio(userProfileDTO.getBio());
					profileCandidateRepository.save(profileUpdate);

					Set<String> speNameList = userProfileDTO.getSpecializationNames();
					Set<Specialization> newSpeList = new HashSet<>();
					for (String speName : speNameList) {
						Specialization specialization = specializationRepository.findBySpecializationName(speName);
						newSpeList.add(specialization);
					}
					user.setSpecializations(newSpeList);
					userRepository.save(user);
					return new ResponseEntity<>("Successfully", HttpStatus.OK);
				}

				if (user.getRoles().getRoleName().equals("employer")) {
					ProfileEmployer profileEmployer = profileEmployerRepository
							.findProfileEmployerByUserId(user.getUserId());
					profileEmployer.setName(userProfileDTO.getName());
					profileEmployer.setCompanyName(userProfileDTO.getCompanyName());
					profileEmployer.setPhoneNumber(userProfileDTO.getPhoneNumber());
					profileEmployer.setAddress(userProfileDTO.getAddress());
					profileEmployer.setBio(userProfileDTO.getBio());
					profileEmployer.setTaxNumber(userProfileDTO.getTaxNumber());
					profileEmployerRepository.save(profileEmployer);

					Set<String> speNameList = userProfileDTO.getSpecializationNames();
					Set<Specialization> newSpeList = new HashSet<>();
					for (String speName : speNameList) {
						Specialization specialization = specializationRepository.findBySpecializationName(speName);
						newSpeList.add(specialization);
					}
					user.setSpecializations(newSpeList);
					userRepository.save(user);
					return new ResponseEntity<>("Successfully", HttpStatus.OK);
				}

			}
		}
		return new ResponseEntity<>(null, HttpStatus.OK);

	}
}
