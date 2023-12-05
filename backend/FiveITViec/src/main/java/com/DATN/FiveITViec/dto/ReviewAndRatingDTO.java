package com.DATN.FiveITViec.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewAndRatingDTO {
	private Long jobId;
	private String userName;
    private int rating;
    private String review_text;
    private LocalDateTime createdAt;
}
