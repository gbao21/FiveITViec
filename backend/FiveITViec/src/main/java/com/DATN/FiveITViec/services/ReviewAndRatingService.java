package com.DATN.FiveITViec.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.DATN.FiveITViec.dto.ReviewAndRatingDTO;
import com.DATN.FiveITViec.model.ReviewAndRating;
import com.DATN.FiveITViec.repository.ReviewAndRatingRepository;

@Service
public class ReviewAndRatingService {

	@Autowired
	private ReviewAndRatingRepository reviewAndRatingRepository;

	public List<ReviewAndRating> getAllReviewAndRating() {
		return reviewAndRatingRepository.findAll();
	}

	public List<ReviewAndRatingDTO> getAllReviewAndRatingByJobId(Long jobId) {
		List<ReviewAndRating> listReviewAndRatings = reviewAndRatingRepository.findListReviewAndRatingByJobId(jobId);
		List<ReviewAndRatingDTO> listDTO = new ArrayList<>();
		for (ReviewAndRating review : listReviewAndRatings) {
			ReviewAndRatingDTO reviewDTO = new ReviewAndRatingDTO();
			reviewDTO.setReview_text(review.getReviewText());
			reviewDTO.setRating(review.getRating());
			listDTO.add(reviewDTO);
		}
		return listDTO;
	}

	public Page<ReviewAndRatingDTO> getAllReviewAndRatingByJobId(Long jobId, Pageable pageable) {
		Page<ReviewAndRating> pageOfReviewAndRating = reviewAndRatingRepository.findReviewAndRatingByJobId(jobId,
				pageable);

		List<ReviewAndRatingDTO> listReviewAndRatingDTO = new ArrayList<>();
		for (ReviewAndRating reviewAndRating : pageOfReviewAndRating.getContent()) {
			ReviewAndRatingDTO reviewAndRatingDTO = new ReviewAndRatingDTO();
			reviewAndRatingDTO.setJobId(reviewAndRating.getJob().getJobId());
			reviewAndRatingDTO.setUserName(reviewAndRating.getUser().getEmail());
			reviewAndRatingDTO.setRating(reviewAndRating.getRating());
			reviewAndRatingDTO.setReview_text(reviewAndRating.getReviewText());
			reviewAndRatingDTO.setCreatedAt(reviewAndRating.getCreatedAt());
			listReviewAndRatingDTO.add(reviewAndRatingDTO);
		}

		return new PageImpl<>(listReviewAndRatingDTO, pageable, pageOfReviewAndRating.getTotalElements());
	}
}
