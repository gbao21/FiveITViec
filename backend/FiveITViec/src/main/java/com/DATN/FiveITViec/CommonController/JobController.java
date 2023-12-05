package com.DATN.FiveITViec.CommonController;

import com.DATN.FiveITViec.model.Job;
import com.DATN.FiveITViec.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
public class JobController {

    @Autowired
    private JobService jobService;
    @GetMapping("/jobsView")
    public List<Job> getAllJobs(){
        return jobService.getAllJobs();
    }
}
