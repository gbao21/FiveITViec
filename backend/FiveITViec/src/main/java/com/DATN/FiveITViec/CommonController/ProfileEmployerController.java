package com.DATN.FiveITViec.CommonController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DATN.FiveITViec.dto.JobDTO;
import com.DATN.FiveITViec.dto.UserManagementDTO;
import com.DATN.FiveITViec.repository.SpecializationRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.JobService;
import com.DATN.FiveITViec.services.UserService;

@RestController
@RequestMapping("/api/company/")
public class ProfileEmployerController {

	@Autowired
	private JWTGenerator jwtGenerator;
	@Autowired
	private JobService jobService;

	@Autowired
	public UserService userService;
	@Autowired
	public SpecializationRepository specializationRepository;

	@GetMapping("/getAllEmployerApproval")
	public ResponseEntity<?> getAllEmployer(@RequestParam(name = "companyName") String companyName,
			@RequestParam(name = "address") String address, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {

		Page<UserManagementDTO> getAllEnableCompany = userService.getEmployerByStatusApproval(companyName, address,
				PageRequest.of(page, size));
		return new ResponseEntity<>(getAllEnableCompany, HttpStatus.OK);
	}

	@GetMapping("/getJobByCompanyEnableApproval")
	public ResponseEntity<?> getAllJob(@RequestParam(name = "userId") Long userId) {
		java.util.List<JobDTO> getAllEnableJob = jobService.getJobByUserId(userId);
		return new ResponseEntity<>(getAllEnableJob, HttpStatus.OK);
	}

}