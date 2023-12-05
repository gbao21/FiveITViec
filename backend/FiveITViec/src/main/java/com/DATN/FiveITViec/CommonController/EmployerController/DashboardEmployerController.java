package com.DATN.FiveITViec.CommonController.EmployerController;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.model.Job;
import com.DATN.FiveITViec.model.ProfileEmployer;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.repository.ApplicantRepository;
import com.DATN.FiveITViec.repository.JobRepository;
import com.DATN.FiveITViec.repository.ProfileEmployerRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.JobService;
import com.DATN.FiveITViec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth/employer")
public class DashboardEmployerController {
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private JobService jobService;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private ProfileEmployerRepository profileEmployerRepository;


    @GetMapping("/getTotalData")
    public ResponseEntity<?> getAllContacts(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader){

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                String email = jwtGenerator.getUsernameFromJWT(token);
                User user = userRepository.findByEmail(email);
                ProfileEmployer profileEmployer = profileEmployerRepository.findProfileEmployerByUserId(user.getUserId());
                String companyName=profileEmployer.getCompanyName();
                Map<String, String> total = new HashMap<>();
                long totalCVWaiting=0;
                long totalCVApproved=0;
                long jobEnable = jobRepository.countAllJobByUser(user.getUserId(),FiveITConstants.ENABLE_STATUS);
                long jobDisable = jobRepository.countAllJobByUser(user.getUserId(),FiveITConstants.DISABLE_STATUS);
                List<Job> listJob = jobRepository.getAllJobByUser(user.getUserId());
                for(int i = 0; i< listJob.size();i++){
                    Job job = listJob.get(i);
                    long cvWaiting = applicantRepository.countAllApplicantByUser(job.getJobId(),FiveITConstants.WAITING_STATUS);
                    totalCVWaiting+=cvWaiting;
                    long cvApproved = applicantRepository.countAllApplicantByUser(job.getJobId(),FiveITConstants.APPROVAL_STATUS);
                    totalCVApproved+=cvApproved;
                }
                total.put("jobEnable",String.valueOf(jobEnable));
                total.put("jobDisable",String.valueOf(jobDisable));
                total.put("cvWaiting",String.valueOf(totalCVWaiting));
                total.put("cvApproved",String.valueOf(totalCVApproved));
                total.put("companyName",companyName);
                return new ResponseEntity<>(total, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("/getQuantityJobAndCvByMonth")
    public  ResponseEntity<?> getQuantityJobAndCvByMonth(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name = "year",required = false) String year){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                Map<Object, Object> totalCv = new HashMap<>();
                Map<Object, Object> totalJob = new HashMap<>();
                Map<String, Object> responseMap = new HashMap<>();
                String email = jwtGenerator.getUsernameFromJWT(token);
                User user = userRepository.findByEmail(email);
                int yearCurrent = 0;
                if(year!=null){
                    yearCurrent= Integer.valueOf(year);
                }else{
                    yearCurrent = YearMonth.now().getYear();
                }
                List<Job> listJob = jobRepository.getAllJobByUserAndYear(user.getUserId(),yearCurrent);
                if(listJob.size()==0){
                    for(int i=1;i<=12;i++){
                        totalCv.put(i,0);
                    }
                }else{
                    for(int i = 0; i<listJob.size();i++){
                        Job job = listJob.get(i);
                        for(int j = 1; j<=12; j++){
                            Long countApplicant = applicantRepository.countAllApplicantByUserAndMonth(job.getJobId(),j,yearCurrent);
                            if (totalCv.containsKey(j)) {
                                totalCv.put(j,(Long)totalCv.get(j) + countApplicant);
                            }else{
                                totalCv.put(j, countApplicant);
                            }
                        }
                    }
                }
                for(int i = 1;i<=12; i++){
                    Long countJob = jobRepository.countAllJobByUserAndMonth(user.getUserId(),i,yearCurrent);
                    totalJob.put(i,countJob);
                }
                responseMap.put("applicantMap",totalCv);
                responseMap.put("jobMap",totalJob);


                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }



}
