package com.DATN.FiveITViec.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CategoryDTO {

	private long categoryId;

	private String categoryName;

	private String categoryImg;
	
	private LocalDateTime createdAt;
}
