package com.DATN.FiveITViec.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.DATN.FiveITViec.model.JobCategory;
import com.DATN.FiveITViec.model.User;

import lombok.Data;

@Data
public class JobDTO {
	private Long jobId;
	private String title;
	private JobCategory jobCategory;
	private Long categoryId;
	private User user;
	private String jobImg;
	private String description;
	private String requirements;
	private double salary;
	private String location;
	private int quantityCv;

	private Date applicationDeadline;
	private String status;
	private String approval;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;

	public JobDTO(Long jobId, String title, JobCategory jobCategory, Long categoryId, User user, String jobImg,
			String description, String requirements, double salary, String location, int quantityCv,
			Date applicationDeadline, String status, String approval, LocalDateTime createdAt, String createdBy,
			LocalDateTime updatedAt, String updatedBy) {
		this.jobId = jobId;
		this.title = title;
		this.jobCategory = jobCategory;
		this.categoryId = categoryId;
		this.user = user;
		this.jobImg = jobImg;
		this.description = description;
		this.requirements = requirements;
		this.salary = salary;
		this.location = location;
		this.quantityCv = quantityCv;
		this.applicationDeadline = applicationDeadline;
		this.status = status;
		this.approval = approval;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.updatedAt = updatedAt;
		this.updatedBy = updatedBy;
	}

	public JobDTO() {
	}

}