package com.DATN.FiveITViec.model;

import java.util.Objects;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Setter
@Getter
@Data
@Table(name = "applicant")
public class Applicant extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private long applicantId;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "job_id", referencedColumnName = "jobId", nullable = true)
	private Job job;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = true)
	private User user;

	private String fullName;
	private String email;
	private String phoneNumber;

	private String cv;

	private String coverletter;

	private String status;
	
	private String message;

	public Applicant(long applicantId, Job job, User user, String fullName, String email, String phoneNumber, String cv,
			String coverletter, String status, String message) {
		super();
		this.applicantId = applicantId;
		this.job = job;
		this.user = user;
		this.fullName = fullName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.cv = cv;
		this.coverletter = coverletter;
		this.status = status;
		this.message = message;
	}

	public Applicant() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Applicant other = (Applicant) obj;
		return applicantId == other.applicantId && Objects.equals(coverletter, other.coverletter)
				&& Objects.equals(cv, other.cv) && Objects.equals(email, other.email)
				&& Objects.equals(fullName, other.fullName) && Objects.equals(job, other.job)
				&& Objects.equals(message, other.message) && Objects.equals(phoneNumber, other.phoneNumber)
				&& Objects.equals(status, other.status) && Objects.equals(user, other.user);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ Objects.hash(applicantId, coverletter, cv, email, fullName, job, message, phoneNumber, status, user);
		return result;
	}

	@Override
	public String toString() {
		return "Applicant [applicantId=" + applicantId + ", job=" + job + ", user=" + user + ", fullName=" + fullName
				+ ", email=" + email + ", phoneNumber=" + phoneNumber + ", cv=" + cv + ", coverletter=" + coverletter
				+ ", status=" + status + ", message=" + message + "]";
	}

	

}