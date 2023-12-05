package com.DATN.FiveITViec.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.DATN.FiveITViec.model.*;

import lombok.Data;

@Data
public class UserProfileDTO {
	private String profileType; // "candidate" or "employer"
	private String name;
	private String email;
	private String userImage;
	private String userCv;
	private String gender;
	private String phoneNumber;
	private String address;
	private String bio;
	private String companyName;
	private String companyLogo;
	private String companyImg1;
	private String companyImg2;
	private String companyImg3;
	private String taxNumber;
	private Set<String> specializationNames;
	private Set<JobDTO> favoriteJobs;

	public UserProfileDTO() {
	}

	public UserProfileDTO(ProfileCandidate candidate, Set<Specialization> specializations, Set<Job> favoriteJobs) {
		this.profileType = "candidate";
		this.name = candidate.getName();
		this.email = candidate.getUser().getEmail();
		this.gender = candidate.getGender();
		this.phoneNumber = candidate.getPhoneNumber();
		this.bio = candidate.getBio();
		this.address = candidate.getAddress();
		this.userImage = candidate.getUserImage();
		this.userCv = candidate.getUserCv();
		this.specializationNames = specializations.stream().map(Specialization::getSpecializationName)
				.collect(Collectors.toSet());
		this.favoriteJobs = favoriteJobs.stream().map(job -> {
			JobDTO jobDTO = new JobDTO();
			jobDTO.setJobId(job.getJobId());
			jobDTO.setTitle(job.getTitle());
			jobDTO.setJobCategory(job.getJobCategory());
			jobDTO.setJobImg(job.getJobImg());
			jobDTO.setDescription(job.getDescription());
			jobDTO.setRequirements(job.getRequirements());
			jobDTO.setSalary(job.getSalary());
			jobDTO.setCreatedAt(job.getCreatedAt());
			jobDTO.setApplicationDeadline(job.getApplicationDeadline());
			jobDTO.setStatus(job.getStatus());
			jobDTO.setQuantityCv(job.getQuantityCv());
			return jobDTO;
		}).collect(Collectors.toSet());
	}

	public UserProfileDTO(ProfileEmployer employer, Set<Specialization> specializations) {
		this.profileType = "employer";
		this.name = employer.getName();
		this.email = employer.getUser().getEmail();
		this.companyName = employer.getCompanyName();
		this.companyLogo = employer.getCompanyLogo();
		this.phoneNumber = employer.getPhoneNumber();
		this.bio = employer.getBio();
		this.address = employer.getAddress();
		this.taxNumber = employer.getTaxNumber();
		this.companyImg1 = employer.getCompanyImg1();
		this.companyImg2 = employer.getCompanyImg2();
		this.companyImg3 = employer.getCompanyImg3();
		// Set other employer-specific fields
		this.specializationNames = specializations.stream().map(Specialization::getSpecializationName)
				.collect(Collectors.toSet());
	}

	public UserProfileDTO(ProfileAdmin admin) {
		this.profileType = "admin";
		this.name = admin.getName();
		this.email = admin.getUser().getEmail();
		this.gender = admin.getGender();
		this.phoneNumber = admin.getPhoneNumber();
		this.bio = admin.getBio();
		this.address = admin.getAddress();
		this.userImage = admin.getUserImage();
	}

}
