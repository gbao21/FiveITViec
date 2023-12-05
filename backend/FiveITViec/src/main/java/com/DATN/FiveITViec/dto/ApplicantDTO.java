package com.DATN.FiveITViec.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApplicantDTO {
	private long applicantId;
	private long jobId;
	private long userId;
	private String fullName;
	private String phoneNumber;
	private String email;
	private String cv;
	private String coverletter;
	private String status;
	private LocalDateTime createdAt;
}
