package com.DATN.FiveITViec.CommonController.EmployerController;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.ApplicantDTO;
import com.DATN.FiveITViec.dto.JobDTO;
import com.DATN.FiveITViec.model.Applicant;
import com.DATN.FiveITViec.model.Job;
import com.DATN.FiveITViec.model.JobCategory;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.repository.JobCategoryRepository;
import com.DATN.FiveITViec.repository.JobRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.ApplicantService;
import com.DATN.FiveITViec.services.FileUploadService;
import com.DATN.FiveITViec.services.JobService;
import com.DATN.FiveITViec.services.UserService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth/employer")
public class JobManagementController {
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private JobService jobService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobCategoryRepository jobCategoryRepository;
    @Autowired
    public FileUploadService fileUploadService;

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ApplicantService applicantService;


    @GetMapping("/getAllJob")
    public ResponseEntity<?> getAllContacts(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name="email") String email,
            @RequestParam(name="status") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size){

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                if(status.equals(FiveITConstants.ENABLE_STATUS)){
                    Page<JobDTO> getAllEnableJob = jobService.getAllJobByUser(email, FiveITConstants.ENABLE_STATUS, PageRequest.of(page, size));
                    return new ResponseEntity<>(getAllEnableJob, HttpStatus.OK);
                }else
                if(status.equals(FiveITConstants.DISABLE_STATUS)){
                    Page<JobDTO> getAllDisableJob = jobService.getAllJobByUser(email, FiveITConstants.DISABLE_STATUS,PageRequest.of(page, size));
                    return new ResponseEntity<>(getAllDisableJob, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }





    @PostMapping("/postJob")
    public ResponseEntity<String> postJob(@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
                                          @RequestParam("title") String title,
                                          @RequestParam("cateId") Long cateId,
                                          @RequestParam("salary") double salary,
                                          @RequestParam("location") String location,
                                          @RequestParam("description") String description,
                                          @RequestParam("requirements") String requirements,
                                          @RequestParam("quantityCv") int quantityCv,
                                          @RequestParam("applicationDeadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date applicationDeadline,
                                          @RequestParam(value = "defaultImg", required = false)String defaultImg,
                                          @RequestParam(value = "file", required = false)MultipartFile jobImg) throws IOException {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                String email = jwtGenerator.getUsernameFromJWT(token);
                User user = userRepository.findByEmail(email);
                String imgURL="";
                System.out.println(defaultImg);
                if(jobImg !=null){
                     imgURL = fileUploadService.uploadFile(jobImg);
                }else{
                    imgURL = defaultImg;
                }
                if(user.getRoles().getRoleName().equals("employer")){
                    Optional<JobCategory> cate = jobCategoryRepository.findById(1L);
                    Job job =new Job();
                    job.setTitle(title);
                    job.setJobImg(imgURL);
                    job.setJobCategory(cate.get());
                    job.setSalary(salary);
                    job.setUser(user);
                    job.setDescription(description);
                    job.setRequirements(requirements);
                    job.setLocation(location);
                    job.setApplicationDeadline(applicationDeadline);
                    job.setQuantityCv(quantityCv);
                    job.setStatus(FiveITConstants.DISABLE_STATUS);
                    job.setApproval(FiveITConstants.WAITING_STATUS);
                    jobRepository.save(job);
                    return new ResponseEntity<>("Save success", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);

    }


    @PutMapping("/updateJob")
    public ResponseEntity<String> updateJob(@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
                                          @RequestParam("jobId") Long jobId,
                                          @RequestParam("title") String title,
                                          @RequestParam("cateId") Long cateId,
                                          @RequestParam("salary") double salary,
                                          @RequestParam("location") String location,
                                          @RequestParam("quantityCv") int quantityCv,
                                          @RequestParam("description") String description,
                                          @RequestParam("requirements") String requirements,
                                          @RequestParam("applicationDeadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date applicationDeadline,
                                            @RequestParam(value = "file", required = false)MultipartFile jobImg) throws IOException {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                String email = jwtGenerator.getUsernameFromJWT(token);
                User user = userRepository.findByEmail(email);
                Job job = jobRepository.findJobByJobId(jobId);
                String imgURL = "";
                if (jobImg != null) {
                    imgURL = fileUploadService.uploadFile(jobImg);
                }else{
                    imgURL = job.getJobImg();
                }

                if (job.getApproval().equals(FiveITConstants.WAITING_STATUS) && user.getRoles().getRoleName().equals("employer")) {
                    Optional<JobCategory> cate = jobCategoryRepository.findById(cateId);
                    job.setTitle(title);
                    job.setJobImg(imgURL);
                    job.setJobCategory(cate.get());
                    job.setSalary(salary);
                    job.setUser(user);
                    job.setDescription(description);
                    job.setRequirements(requirements);
                    job.setLocation(location);
                    job.setApplicationDeadline(applicationDeadline);
                    job.setQuantityCv(quantityCv);
                    job.setStatus(FiveITConstants.DISABLE_STATUS);
                    job.setApproval(FiveITConstants.WAITING_STATUS);
                    jobRepository.save(job);
                    return new ResponseEntity<>("Save success", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/closeJob")
    public ResponseEntity<?> closeJob(@RequestHeader(name="Authorization", required = false)String authorizationHeader,
                                                 @RequestParam("jobId")Long jobId){
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                if (jwtGenerator.validateToken(token)) {
                    String email = jwtGenerator.getUsernameFromJWT(token);
                    User user = userRepository.findByEmail(email);
                    Job job = jobRepository.findJobByJobId(jobId);
                    if(job !=null && job.getStatus().equals(FiveITConstants.ENABLE_STATUS)){
                        job.setStatus(FiveITConstants.DISABLE_STATUS);
                        job.setApproval(FiveITConstants.APPROVAL_STATUS);
                        jobRepository.save(job);
                        return new ResponseEntity<>("Success", HttpStatus.OK);
                    }else{
                        return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
                    }
                    }
                }
            return new ResponseEntity<>("Fail", HttpStatus.UNAUTHORIZED);
    }



    @GetMapping("/searchJob")
    public ResponseEntity<?> getAllContacts(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name="startDate",required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name="endDate",required = false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name="query") String query,
            @RequestParam(name="status") String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size){

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                String email = jwtGenerator.getUsernameFromJWT(token);
                User user = userRepository.findByEmail(email);
                if(status.equals(FiveITConstants.ENABLE_STATUS)){
                    Page<JobDTO> getAllEnableJob = jobService.searchAllJobByCondition(email, FiveITConstants.ENABLE_STATUS,startDate,endDate,query, PageRequest.of(page, size));
                    return new ResponseEntity<>(getAllEnableJob, HttpStatus.OK);
                }else
                if(status.equals(FiveITConstants.DISABLE_STATUS)){
                    Page<JobDTO> getAllDisableJob = jobService.searchAllJobByCondition(email, FiveITConstants.DISABLE_STATUS,startDate,endDate,query, PageRequest.of(page, size));
                    return new ResponseEntity<>(getAllDisableJob, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/getJobApplicationDeadline")
    public ResponseEntity<?> getAllJobApplicationDeadline(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name="applicationDeadline") LocalDate applicationDeadline){

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                String email = jwtGenerator.getUsernameFromJWT(token);
                User user = userRepository.findByEmail(email);
                if(user!=null){
                    List<JobDTO> listJobDTO = jobService.getAllJobByApplicationDeadline(email, applicationDeadline);
//                    for (JobDTO job : listJobDTO) {
//                        Job jobClose = jobRepository.findJobByJobId(job.getJobId());
//                        if(jobClose.getStatus().equals(FiveITConstants.ENABLE_STATUS)){
//                        jobClose.setStatus(FiveITConstants.DISABLE_STATUS);
//                        jobRepository.save(jobClose);
//                        System.out.println(jobClose);
//                        }
//                    }
                    return new ResponseEntity<>(listJobDTO,  HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("/closeJobDeadline")
    public ResponseEntity<?> closeJobDeadline(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name="applicationDeadline") LocalDate applicationDeadline){

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                String email = jwtGenerator.getUsernameFromJWT(token);
                User user = userRepository.findByEmail(email);
                if(user!=null){
                    List<JobDTO> listJobDTO = jobService.getAllJobByApplicationDeadline(email, applicationDeadline);
                    for (JobDTO job : listJobDTO) {
                        Job jobClose = jobRepository.findJobByJobId(job.getJobId());
                        if(jobClose.getStatus().equals(FiveITConstants.ENABLE_STATUS)){
                        jobClose.setStatus(FiveITConstants.DISABLE_STATUS);
                        jobRepository.save(jobClose);
                        System.out.println(jobClose);
                        }
                    }
                    return new ResponseEntity<>(listJobDTO,  HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }


}
