package com.DATN.FiveITViec.CommonController.EmployerController;

import com.DATN.FiveITViec.dto.ApplicantDTO;
import com.DATN.FiveITViec.dto.ReviewAndRatingDTO;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.repository.JobRepository;
import com.DATN.FiveITViec.repository.ReviewAndRatingRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.ReviewAndRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/employer")
public class ReviewManagementController {
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ReviewAndRatingService reviewAndRatingService;

@GetMapping("/getReview")
    public ResponseEntity<?> getAllReview(@RequestHeader(name="Authorization", required = false) String authorizationHeader,
                                                              @RequestParam("jobId") Long jobId) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                String email = jwtGenerator.getUsernameFromJWT(token);
                List<ReviewAndRatingDTO> listApplicant = reviewAndRatingService.getAllReviewAndRatingByJobId(jobId);
                    return new ResponseEntity<>(listApplicant,HttpStatus.OK);
            }
            return new ResponseEntity<>("Fail",HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>("Fail",HttpStatus.UNAUTHORIZED);

    }
}
