package com.DATN.FiveITViec.CommonController;

import java.util.List;
import java.util.Optional;

import com.DATN.FiveITViec.repository.*;
import com.DATN.FiveITViec.dto.BlogCommentDTO;
import com.DATN.FiveITViec.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DATN.FiveITViec.dto.ReviewAndRatingDTO;
import com.DATN.FiveITViec.dto.UserProfileDTO;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.ReviewAndRatingService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000/")
public class ReviewAndRatingController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ReviewAndRatingRepository reviewandRatingRepository;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private ReviewAndRatingService reviewAndRatingService;
    @Autowired
    private BlogCommentRepository blogCommentRepository;
  @Autowired
    private BlogRepository blogRepository;

    @GetMapping("/viewComments")
    public List<ReviewAndRating> getAllJobs() {
        return reviewAndRatingService.getAllReviewAndRating();
    }
    @GetMapping("/viewBlogComments")
    public List<BlogComment> getAllBlogComment() {
        return blogCommentRepository.findAll();
    }

    @PostMapping("/postComment")
    public ResponseEntity<UserProfileDTO> responseEntity(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestBody ReviewAndRatingDTO reviewAndRatingDTO) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                String email = jwtGenerator.getUsernameFromJWT(token);
                User user = userRepository.findByEmail(email);
                Long jobId = reviewAndRatingDTO.getJobId();
                Optional<Job> job = jobRepository.findById(jobId);
                if (user != null && job.get() != null) {
                    ReviewAndRating reviewAndRating = new ReviewAndRating();
                    reviewAndRating.setJob(job.get());
                    reviewAndRating.setUser(user);
                    reviewAndRating.setRating(reviewAndRatingDTO.getRating());
                    reviewAndRating.setReviewText(reviewAndRatingDTO.getReview_text());
                    reviewandRatingRepository.save(reviewAndRating);
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

//    blogComment

    @PostMapping("/postBlogComment")
    public ResponseEntity<UserProfileDTO> postBlogComment(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestBody BlogCommentDTO blogCommentDTO) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                String email = jwtGenerator.getUsernameFromJWT(token);
                User user = userRepository.findByEmail(email);
                Long blogId = blogCommentDTO.getBlogId();
                Optional<Blog> blog = blogRepository.findById(blogId);
                if (user != null && blog.get() != null) {
                    BlogComment blogComment = new BlogComment();
                    blogComment.setBlog(blog.get());
                    blogComment.setUser(user);
                    blogComment.setRating(blogCommentDTO.getRating());
                    blogComment.setCommentText(blogCommentDTO.getBlogComment());
                    blogCommentRepository.save(blogComment);
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

}
