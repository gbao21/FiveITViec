package com.DATN.FiveITViec.CommonController;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DATN.FiveITViec.dto.JobDTO;
import com.DATN.FiveITViec.model.Job;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.repository.JobRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.security.JWTGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000/")
public class FavoriteJobController {

	@Autowired
	private JWTGenerator jwtGenerator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JobRepository jobRepository;

	@PostMapping("/actionFavoriteJob")
	public ResponseEntity<?> actionFavoriteJob(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam("jobId") String jobId) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				String email = jwtGenerator.getUsernameFromJWT(token);
				User user = userRepository.findByEmail(email);
				if (user != null) {
					Job job = jobRepository.findJobByJobId( Long.parseLong(jobId));
					Set<Job> jobList = user.getJobs();
					String message = "";
					if (jobList != null) {
						boolean jobExistsInList = false;
						for (Job userJob : jobList) {
							if (userJob.getJobId() == Long.parseLong(jobId)) {
								jobExistsInList = true;
								break;
							}
						}
						if (jobExistsInList) {
							jobList.remove(job);
							message = "Remove success";
						} else {
							jobList.add(job);
							message = "Add success";
						}
					}else {
						jobList.add(job);
						message = "Add success";
					}
					user.setJobs(jobList);
					userRepository.save(user);
					return new ResponseEntity<>(message, HttpStatus.OK);
				}
			}
		}
		// Trả về mã lỗi 401 (UNAUTHORIZED) nếu không xác thực.
		return new ResponseEntity<>("authentication failed", HttpStatus.UNAUTHORIZED);
	}
}
