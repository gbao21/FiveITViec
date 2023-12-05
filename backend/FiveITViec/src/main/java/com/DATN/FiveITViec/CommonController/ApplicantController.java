package com.DATN.FiveITViec.CommonController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.DATN.FiveITViec.dto.ApplicantDTO;
import com.DATN.FiveITViec.dto.UserProfileDTO;
import com.DATN.FiveITViec.model.Applicant;
import com.DATN.FiveITViec.model.Job;
import com.DATN.FiveITViec.model.ProfileCandidate;
import com.DATN.FiveITViec.model.ProfileEmployer;
import com.DATN.FiveITViec.model.Specialization;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.repository.ApplicantRepository;
import com.DATN.FiveITViec.repository.JobRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.FileUploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000/")
public class ApplicantController {

	@Autowired
	private JWTGenerator jwtGenerator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private ApplicantRepository applicantRepository;

	@Autowired
	public FileUploadService fileUploadService;

	@GetMapping("/loadApply")
	public ResponseEntity<?> loadApply(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
	    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	        String token = authorizationHeader.substring(7);
	        if (jwtGenerator.validateToken(token)) {
	            String email = jwtGenerator.getUsernameFromJWT(token);
	            User user = userRepository.findByEmail(email);
	            if (user != null) {
	                String role = user.getRoles().getRoleName();
	                List<Applicant> response = applicantRepository.findApplicantByUserId(user.getUserId());
	                List<ApplicantDTO> list = new ArrayList<>();
	                for (Applicant applicant : response) {
	                    ApplicantDTO applicantDTO = new ApplicantDTO(); 
	                    applicantDTO.setApplicantId(applicant.getApplicantId());             
	                    applicantDTO.setJobId(applicant.getJob().getJobId());             
	                    applicantDTO.setUserId(applicant.getUser().getUserId());             
	                    applicantDTO.setFullName(applicant.getFullName());             
	                    applicantDTO.setEmail(applicant.getEmail());             
	                    applicantDTO.setPhoneNumber(applicant.getPhoneNumber());             
	                    applicantDTO.setCv(applicant.getCv());             
	                    applicantDTO.setCoverletter(applicant.getCoverletter());             
	                    applicantDTO.setStatus(applicant.getStatus());             
	                    list.add(applicantDTO); 
	                }
	                if (!list.isEmpty()) {
	                    return new ResponseEntity<>(list, HttpStatus.OK);
	                } else {
	                    // Trả về mã lỗi 404 (Not Found) hoặc một thông báo lỗi tùy thuộc vào yêu cầu.
	                    return new ResponseEntity<>(null, HttpStatus.OK);
	                }
	            }
	        }
	    }
	    // Trả về mã lỗi 401 (UNAUTHORIZED) nếu không xác thực.
	    return new ResponseEntity<>("Xác thực không thành công", HttpStatus.UNAUTHORIZED);
	}


	@PostMapping("/applyJob")
	public ResponseEntity<String> uploadFile(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam("jobId") String jobId, @RequestParam("cv") Object cvFile,
			@RequestParam("fullName") String fullName, @RequestParam("email") String emailApply,
			@RequestParam("phoneNumber") String phoneNumber, @RequestParam("coverletter") String coverletter)
			throws IOException {

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				String email = jwtGenerator.getUsernameFromJWT(token);
				User user = userRepository.findByEmail(email);
				Optional<Job> job = jobRepository.findById(Long.parseLong(jobId));

				Applicant applicant = new Applicant();
				applicant.setJob(job.get());
				applicant.setUser(user);
				applicant.setFullName(fullName);
				applicant.setEmail(emailApply);
				applicant.setPhoneNumber(phoneNumber);
				applicant.setCoverletter(coverletter);
				applicant.setStatus("WAITING");
				if (cvFile instanceof String) {
					String cvAsString = (String) cvFile;
					applicant.setCv(cvAsString);
				} else {
					MultipartFile cvMultipartFile = (MultipartFile) cvFile;
					String fileURL = fileUploadService.uploadFile(cvMultipartFile); // Lưu trữ tệp tải lên
					if (isPdfFile(cvMultipartFile)) {
						applicant.setCv(fileURL);
					}
				}
				applicantRepository.save(applicant);
				return new ResponseEntity<>("Successfully", HttpStatus.OK);
			}
		}

		return new ResponseEntity<>("Upload Fail", HttpStatus.BAD_REQUEST);
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

}
