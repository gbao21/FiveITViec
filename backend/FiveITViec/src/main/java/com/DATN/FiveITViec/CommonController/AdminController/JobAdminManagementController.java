package com.DATN.FiveITViec.CommonController.AdminController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.ApplicantDTO;
import com.DATN.FiveITViec.dto.JobDTO;
import com.DATN.FiveITViec.dto.ReviewAndRatingDTO;
import com.DATN.FiveITViec.model.Job;
import com.DATN.FiveITViec.repository.JobRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.ApplicantService;
import com.DATN.FiveITViec.services.JobService;
import com.DATN.FiveITViec.services.ReviewAndRatingService;

@RestController()
@RequestMapping("/auth/admin")
public class JobAdminManagementController {
	@Autowired
	private JobService jobService;

	@Autowired
	private ApplicantService applicantService;

	@Autowired
	private ReviewAndRatingService reviewAndRatingService;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private JWTGenerator jwtGenerator;

	@GetMapping("/getAllJobs")
	public ResponseEntity<?> getAllJobs(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam(name = "jobTitle") String jobTitle, @RequestParam(name = "status") String status,
			@RequestParam(name = "approval") String approval,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				Page<JobDTO> getAllBlogs = jobService.getAllBlogsByTitleContainingAndStatusAndApproval(jobTitle, status,
						approval, startDate, endDate, PageRequest.of(page, size));
				return new ResponseEntity<>(getAllBlogs, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Error", HttpStatus.UNAUTHORIZED);
	}

	@PutMapping("/updateJobStatus")
	public ResponseEntity<String> updateJobStatus(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam(name = "jobId") String blogId, @RequestParam(name = "status") String status,
			@RequestParam(name = "approval", required = false) String approval) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				Optional<Job> job = jobRepository.findById(Long.valueOf(blogId));
				if (job.isPresent()) {
					if (approval != null && approval.equals(FiveITConstants.WAITING_STATUS)) {
						job.get().setStatus(FiveITConstants.ENABLE_STATUS);
						job.get().setApproval(FiveITConstants.APPROVAL_STATUS);
					} else {
						job.get().setStatus(status);
					}
					jobRepository.save(job.get());
					return new ResponseEntity<>("Updated", HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<>("Error", HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/getAllApplicantByJob")
	public ResponseEntity<?> getAllApplicantsByJob(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam(name = "jobId") String jobId, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				Page<ApplicantDTO> getAllApplyByJob = applicantService.getAllApplicantByJobId(jobId,
						PageRequest.of(page, size));
				return new ResponseEntity<>(getAllApplyByJob, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Error", HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/getAllReviewAndRatingByJob")
	public ResponseEntity<?> getAllReviewAndRatingByJob(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam(name = "jobId") Long jobId, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				Page<ReviewAndRatingDTO> getReviewByJob = reviewAndRatingService.getAllReviewAndRatingByJobId(jobId,
						PageRequest.of(page, size));
				return new ResponseEntity<>(getReviewByJob, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Error", HttpStatus.UNAUTHORIZED);
	}


	@GetMapping("/getTotalApprovedJobByMonth")
	public  ResponseEntity<?> getTotalApprovedJobByMonth(
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
				Map<Object, Object> totalApprovedJob = new HashMap<>();
				Map<Object, Object> responseMap = new HashMap<>();
				for(int i = 1;i<=12; i++){
					long job = jobRepository.countAllJobByMonthAndStatus(FiveITConstants.APPROVAL_STATUS,i,yearCurrent);
					totalApprovedJob.put(i,job);

				}
				responseMap.put("approvedJobMap",totalApprovedJob);


				return new ResponseEntity<>(responseMap, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/getTotalWaitingJobByMonth")
	public  ResponseEntity<?> getTotalWaitingJobByMonth(
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
				Map<Object, Object> totalWaitingJob = new HashMap<>();
				Map<Object, Object> responseMap = new HashMap<>();
				for(int i = 1;i<=12; i++){
					long job = jobRepository.countAllJobByMonthAndStatus(FiveITConstants.WAITING_STATUS,i,yearCurrent);
					totalWaitingJob.put(i,job);

				}
				responseMap.put("waitingJobMap",totalWaitingJob);


				return new ResponseEntity<>(responseMap, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
	}
}
