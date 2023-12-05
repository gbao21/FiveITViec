package com.DATN.FiveITViec.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.DATN.FiveITViec.dto.JobDTO;
import com.DATN.FiveITViec.model.Job;
import com.DATN.FiveITViec.repository.JobRepository;

@Service
public class JobService {

	@Autowired
	private JobRepository jobRepository;

	public List<Job> getAllJobs() {
		return jobRepository.findAll();
	}

	public Page<JobDTO> getAllJobByUser(String email, String status, Pageable pageable) {
		Page<Job> pageJob = jobRepository.getAllJobByUserAndStatus(email, status, pageable);
		List<JobDTO> listJobDTO = new ArrayList<>();
		for (Job job : pageJob.getContent()) {
			JobDTO jobDTO = new JobDTO();
			jobDTO.setJobId(job.getJobId());
			jobDTO.setTitle(job.getTitle());
			jobDTO.setJobCategory(job.getJobCategory());
			jobDTO.setJobImg(job.getJobImg());
			jobDTO.setSalary(job.getSalary());
			jobDTO.setLocation(job.getLocation());
			jobDTO.setRequirements(job.getRequirements());
			jobDTO.setDescription(job.getDescription());
			jobDTO.setApplicationDeadline(job.getApplicationDeadline());
			jobDTO.setApproval(job.getApproval());
			jobDTO.setStatus(job.getStatus());
			jobDTO.setCreatedAt(job.getCreatedAt());
			jobDTO.setCreatedBy(job.getCreatedBy());
			jobDTO.setQuantityCv(job.getQuantityCv());
			listJobDTO.add(jobDTO);
		}
		return new PageImpl<>(listJobDTO, pageable, pageJob.getTotalElements());

	}

	public Page<JobDTO> searchAllJobByCondition(String email, String status, LocalDate startDate, LocalDate endDate,
			String query, Pageable pageable) {
		Page<Job> pageJob = jobRepository.getAllJobByUserAndSearchByKeyWorkByDateBySta(email, status, startDate,
				endDate, query, pageable);
		List<JobDTO> listJobDTO = new ArrayList<>();
		for (Job job : pageJob.getContent()) {
			JobDTO jobDTO = new JobDTO();
			jobDTO.setJobId(job.getJobId());
			jobDTO.setTitle(job.getTitle());
			jobDTO.setJobCategory(job.getJobCategory());
			jobDTO.setJobImg(job.getJobImg());
			jobDTO.setSalary(job.getSalary());
			jobDTO.setLocation(job.getLocation());
			jobDTO.setRequirements(job.getRequirements());
			jobDTO.setDescription(job.getDescription());
			jobDTO.setApplicationDeadline(job.getApplicationDeadline());
			jobDTO.setApproval(job.getApproval());
			jobDTO.setStatus(job.getStatus());
			jobDTO.setCreatedAt(job.getCreatedAt());
			jobDTO.setCreatedBy(job.getCreatedBy());
			jobDTO.setQuantityCv(job.getQuantityCv());
			listJobDTO.add(jobDTO);
		}
		return new PageImpl<>(listJobDTO, pageable, pageJob.getTotalElements());

	}

	public List<JobDTO> getAllJobByApplicationDeadline(String email, LocalDate applicationDeadline) {
		List<Job> listJob = jobRepository.getAllJobByApplicationDeadline(email, applicationDeadline);
		List<JobDTO> listJobDTO = new ArrayList<>();
		for (Job job : listJob) {
			JobDTO jobDTO = new JobDTO();
			jobDTO.setJobId(job.getJobId());
			jobDTO.setTitle(job.getTitle());
			jobDTO.setJobCategory(job.getJobCategory());
			jobDTO.setJobImg(job.getJobImg());
			jobDTO.setSalary(job.getSalary());
			jobDTO.setLocation(job.getLocation());
			jobDTO.setRequirements(job.getRequirements());
			jobDTO.setDescription(job.getDescription());
			jobDTO.setApplicationDeadline(job.getApplicationDeadline());
			jobDTO.setApproval(job.getApproval());
			jobDTO.setStatus(job.getStatus());
			jobDTO.setCreatedAt(job.getCreatedAt());
			jobDTO.setCreatedBy(job.getCreatedBy());
			jobDTO.setQuantityCv(job.getQuantityCv());
			listJobDTO.add(jobDTO);
		}
		return listJobDTO;

	}

	public Page<JobDTO> getAllBlogsByTitleContainingAndStatusAndApproval(String jobTitle, String status,
			String approval, LocalDate startDate, LocalDate endDate, Pageable pageable) {
		Page<Job> pageOfJobs = jobRepository.getAllJobByTitleContainingAndStatusAndApproval(jobTitle, status, approval,
				startDate, endDate, pageable);

		List<JobDTO> listJobDTO = new ArrayList<>();
		for (Job job : pageOfJobs.getContent()) {
			JobDTO jobDTO = new JobDTO();
			jobDTO.setJobId(job.getJobId());
			jobDTO.setTitle(job.getTitle());
			jobDTO.setJobCategory(job.getJobCategory());
			jobDTO.setJobImg(job.getJobImg());
			jobDTO.setSalary(job.getSalary());
			jobDTO.setLocation(job.getLocation());
			jobDTO.setRequirements(job.getRequirements());
			jobDTO.setDescription(job.getDescription());
			jobDTO.setApplicationDeadline(job.getApplicationDeadline());
			jobDTO.setApproval(job.getApproval());
			jobDTO.setStatus(job.getStatus());
			jobDTO.setCreatedAt(job.getCreatedAt());
			jobDTO.setCreatedBy(job.getCreatedBy());
			jobDTO.setQuantityCv(job.getQuantityCv());
			listJobDTO.add(jobDTO);
		}

		return new PageImpl<>(listJobDTO, pageable, pageOfJobs.getTotalElements());
	}
	
	public List<JobDTO> getJobByUserId(Long userId ){
        List<Job> listJob = jobRepository.findJobsByUserIdAndStatusAndApproval(userId);
        List<JobDTO> listJobDTO = new ArrayList<>();
        for (Job job : listJob) {
            JobDTO jobDTO = new JobDTO();
            jobDTO.setJobId(job.getJobId());
            jobDTO.setTitle(job.getTitle());
            jobDTO.setJobCategory(job.getJobCategory());
            jobDTO.setJobImg(job.getJobImg());
            jobDTO.setSalary(job.getSalary());
            jobDTO.setLocation(job.getLocation());
            jobDTO.setRequirements(job.getRequirements());
            jobDTO.setDescription(job.getDescription());
            jobDTO.setApplicationDeadline(job.getApplicationDeadline());
            jobDTO.setApproval(job.getApproval());
            jobDTO.setStatus(job.getStatus());
            jobDTO.setCreatedAt(job.getCreatedAt());
            jobDTO.setCreatedBy(job.getCreatedBy());
            jobDTO.setQuantityCv(job.getQuantityCv());
            listJobDTO.add(jobDTO);
        }
        return listJobDTO;

    }
}
