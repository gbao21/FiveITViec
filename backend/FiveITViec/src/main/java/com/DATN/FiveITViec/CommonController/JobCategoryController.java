package com.DATN.FiveITViec.CommonController;

import com.DATN.FiveITViec.repository.JobCategoryRepository;
import com.DATN.FiveITViec.model.JobCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
public class JobCategoryController {
	@Autowired
    private JobCategoryRepository jobCategoryRepository;
    @GetMapping("/api/jobCate")
    public List<JobCategory> getAllJobCate(){
        return jobCategoryRepository.findAll();
    }

}
