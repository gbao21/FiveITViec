package com.DATN.FiveITViec.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.DATN.FiveITViec.model.JobCategory;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
	@Query("SELECT o FROM JobCategory o WHERE o.categoryName LIKE %:categoryTitle% "
			+ "AND (:startDate IS NULL OR DATE(o.createdAt) >= :startDate) "
			+ "AND (:endDate IS NULL OR DATE(o.createdAt) <= :endDate)")
	Page<JobCategory> findCatesByTitleContaining(@RequestParam("categoryTitle") String categoryTitle,
												 @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate,
												 Pageable pageable);

	@Query("SELECT o FROM JobCategory o WHERE o.categoryName LIKE %:categoryTitle% ")
	JobCategory findCatesByTitle(@RequestParam("categoryTitle") String categoryTitle);

	@Query("SELECT o FROM JobCategory o WHERE o.categoryName = :categoryName")
	JobCategory findCateByName(@RequestParam("categoryName") String categoryName);
}