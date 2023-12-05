package com.DATN.FiveITViec.repository;

import com.DATN.FiveITViec.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByTitleContaining(@RequestParam ("title") String title, Pageable pageable);

    Page<Job> findByLocationContaining(@RequestParam ("location") String location, Pageable pageable);

    @Query("SELECT o FROM Job o WHERE o.title LIKE %:title% AND (:categoryId IS NULL OR o.jobCategory.categoryId = :categoryId) AND o.location LIKE %:location% AND o.status = 'ENABLE' ORDER BY o.title DESC")
    Page<Job> findJobsByTitleContainingAndCategoryIdAndLocationContaining(@RequestParam("title") String title,@RequestParam("categoryId") Long categoryId,@RequestParam("location")  String location, Pageable pageable);

    @Query("select o from Job o where o.user.email=:email and (o.status LIKE %:status%)")
    Page<Job> getAllJobByUserAndStatus(@RequestParam("email") String email, @RequestParam("status") String status, Pageable pageable);
    @Query("select o from Job o where o.user.email=:email and (o.status LIKE %:status%)"+
            "AND (:startDate IS NULL OR DATE(o.createdAt) >= :startDate) "+
            "AND (:endDate IS NULL OR DATE(o.createdAt) <= :endDate)"+
            "AND (o.title LIKE %:query% OR o.createdBy LIKE %:query% OR o.jobCategory.categoryName LIKE %:query%)"
    )
    Page<Job> getAllJobByUserAndSearchByKeyWorkByDateBySta(@RequestParam("email") String email, @RequestParam("status") String status,
                                                           @RequestParam("startDate") LocalDate startDate,
                                                           @RequestParam("endDate") LocalDate endDate,
                                                           @RequestParam("query") String query,Pageable pageable);


    @Query("select o from Job o where o.user.email=:email and o.status like 'ENABLE' and DATE(o.applicationDeadline)=:applicationDeadline")
    List<Job> getAllJobByApplicationDeadline(@RequestParam("email") String email,@RequestParam("applicationDeadline") LocalDate applicationDeadline);

    @Query("select count(o) from Job o where o.jobCategory.categoryId=:categoryId")
    Integer  getCountJobByCate(@RequestParam ("categoryId") String categoryId);

    @Query("select o from Job o where jobId=:jobId")
    Job findJobByJobId(@Param("jobId")Long jobId);
    
    @Query("SELECT o FROM Job o WHERE o.title LIKE %:jobTitle% AND o.status LIKE %:status% AND o.approval LIKE %:approval% " +
            "AND (:startDate IS NULL OR DATE(o.createdAt) >= :startDate) " +
            "AND (:endDate IS NULL OR DATE(o.createdAt) <= :endDate)")
    Page<Job> getAllJobByTitleContainingAndStatusAndApproval(@RequestParam("jobTitle") String jobTitle, @RequestParam("status") String status, @RequestParam("approval") String approval, @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate , Pageable pageable );
    
    
    @Query("SELECT j FROM Job j WHERE j.user.userId = :userId AND j.status = 'ENABLE' AND j.approval = 'APPROVED'")
    List<Job> findJobsByUserIdAndStatusAndApproval(@RequestParam("userId") Long userId);

    @Query("SELECT COUNT(o.jobId) FROM Job o")
    long countAllJob();

    @Query("SELECT COUNT(o.jobId) FROM Job o where o.user.userId= :userId and o.status= :status")
    long countAllJobByUser(@RequestParam("userId") Long userId,@RequestParam("status") String status);
    @Query("SELECT COUNT(o.jobId) FROM Job o where o.user.userId= :userId and MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
    long countAllJobByUserAndMonth(@RequestParam("userId") Long userId,@RequestParam("month") int month,@RequestParam("year") int year);
    @Query("SELECT o FROM Job o WHERE o.user.userId = :userId and YEAR(o.createdAt) = :year")
    List<Job> getAllJobByUserAndYear(@RequestParam("userId") Long userId,@RequestParam("year") int year);

    @Query("SELECT j FROM Job j WHERE j.user.userId = :userId")
    List<Job> getAllJobByUser(@RequestParam("userId") Long userId);

    @Query("SELECT COUNT(o.jobId) FROM Job o where o.approval= :approval and MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
    long countAllJobByMonthAndStatus(@RequestParam("approval")String approval,@RequestParam("month") int month,@RequestParam("year") int year);

    @Query("SELECT COUNT(o.jobId) FROM Job o where MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
    long countAllJobByMonth(@RequestParam("month") int month,@RequestParam("year") int year);
}
