package com.DATN.FiveITViec.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.DATN.FiveITViec.model.Applicant;
import org.springframework.web.bind.annotation.RequestParam;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

	@Query("SELECT o FROM Applicant o WHERE o.user.userId= :userId ORDER BY o.createdAt DESC")
	List<Applicant> findApplicantByUserId(@Param("userId") Long userId);
	
	@Query("select o from Applicant o where o.job.jobId=:jobId and o.status like 'WAITING'")
    List<Applicant> getAllApplicantByJobId(@Param("jobId") Long jobId);
	
    @Query("select o from Applicant o where o.job.jobId=:jobId and o.status like 'APPROVED'")
    List<Applicant> getAllApplicantApprovedByJobId(@Param("jobId") Long jobId);
    
    @Query("select o from Applicant o where o.job.jobId=:jobId and o.status like 'CLOSE'")
    List<Applicant> getAllApplicantCloseByJobId(@Param("jobId") Long jobId);
    
    @Query("select o from Applicant o where o.job.jobId=:jobId")
    Page<Applicant> findAllApplicantByJobId(@Param("jobId") String jobId, Pageable pageable);
    
    @Query("SELECT o FROM Applicant o WHERE o.job.jobId= :jobId")
	Page<Applicant> findApplicantByJobId(@Param("jobId") Long jobId,Pageable pageable);
    @Query("SELECT count(o) FROM Applicant o WHERE o.job.jobId= :jobId and o.status= :status")
    long countAllApplicantByUser(@Param("jobId") Long jobId,@Param("status") String status);

    @Query("SELECT count(o) FROM Applicant o WHERE o.job.jobId= :jobId and MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
    long countAllApplicantByUserAndMonth(@Param("jobId") Long jobId, @RequestParam("month") int month, @RequestParam("year") int year);

    @Query("SELECT count(o) FROM Applicant o")
    long countAllApplicant();

    @Query("SELECT count(o) FROM Applicant o WHERE MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
    long countAllApplicantByMonth(@RequestParam("month") int month, @RequestParam("year") int year);

}
