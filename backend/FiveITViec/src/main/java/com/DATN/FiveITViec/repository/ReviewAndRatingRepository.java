package com.DATN.FiveITViec.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.DATN.FiveITViec.model.ReviewAndRating;

@Repository
public interface ReviewAndRatingRepository extends JpaRepository<ReviewAndRating, Long> {
	// ReviewAndRating findById(Long id);
//	@Query("SELECT o FROM ReviewAndRating o WHERE o.job.jobId = :jobId ORDER BY o.createdAt DESC")
//	Page<ReviewAndRating> findReviewAndRatingByJobId(@RequestParam("jobId") String jobId, Pageable pageable);

	@Query("SELECT o FROM ReviewAndRating o WHERE o.job.jobId = :jobId ORDER BY o.createdAt DESC")
	List<ReviewAndRating> findListReviewAndRatingByJobId(@RequestParam("jobId") Long jobId);

	@Query("SELECT o FROM ReviewAndRating o WHERE o.job.jobId = :jobId ORDER BY o.createdAt DESC")
	Page<ReviewAndRating> findReviewAndRatingByJobId(@RequestParam("jobId") Long jobId, Pageable pageable);

//    @Query("SELECT o FROM ReviewAndRating o WHERE o.job.jobId = :jobId ORDER BY o.createdAt DESC")
//    List<ReviewAndRating> findListReviewAndRatingByJobId(@RequestParam("jobId") Long jobId);

}
