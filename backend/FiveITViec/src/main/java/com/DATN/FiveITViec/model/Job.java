package com.DATN.FiveITViec.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Entity
@Data
public class Job extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private long jobId;

	private String title;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = true)
	private JobCategory jobCategory;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = true)
	private User user;

	@ManyToMany(mappedBy = "jobs", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Set<User> users = new HashSet<>();

	private String jobImg;

	private String description;

	private String requirements;

	private double salary;

	private String location;

	private Date applicationDeadline;

	private String status;

	private String approval;

	private int quantityCv;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job other = (Job) obj;
		if (applicationDeadline == null) {
			if (other.applicationDeadline != null)
				return false;
		} else if (!applicationDeadline.equals(other.applicationDeadline))
			return false;
		if (approval == null) {
			if (other.approval != null)
				return false;
		} else if (!approval.equals(other.approval))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (jobCategory == null) {
			if (other.jobCategory != null)
				return false;
		} else if (!jobCategory.equals(other.jobCategory))
			return false;
		if (jobId != other.jobId)
			return false;
		if (jobImg == null) {
			if (other.jobImg != null)
				return false;
		} else if (!jobImg.equals(other.jobImg))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (quantityCv != other.quantityCv)
			return false;
		if (requirements == null) {
			if (other.requirements != null)
				return false;
		} else if (!requirements.equals(other.requirements))
			return false;
		if (Double.doubleToLongBits(salary) != Double.doubleToLongBits(other.salary))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((applicationDeadline == null) ? 0 : applicationDeadline.hashCode());
		result = prime * result + ((approval == null) ? 0 : approval.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((jobCategory == null) ? 0 : jobCategory.hashCode());
		result = prime * result + (int) (jobId ^ (jobId >>> 32));
		result = prime * result + ((jobImg == null) ? 0 : jobImg.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + quantityCv;
		result = prime * result + ((requirements == null) ? 0 : requirements.hashCode());
		long temp;
		temp = Double.doubleToLongBits(salary);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		return result;
	}

}
