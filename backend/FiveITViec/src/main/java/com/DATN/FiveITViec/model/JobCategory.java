package com.DATN.FiveITViec.model;


import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="jobcategory")
@Slf4j
public class JobCategory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column( name="category_id")
	private long categoryId;

	private String categoryName;

	private String categoryImg;

}
