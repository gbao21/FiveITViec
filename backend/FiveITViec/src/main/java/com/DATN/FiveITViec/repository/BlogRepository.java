package com.DATN.FiveITViec.repository;

import com.DATN.FiveITViec.model.Blog;
import com.DATN.FiveITViec.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("SELECT o FROM Blog o WHERE o.user.userId  =:userId AND o.status =:status AND o.approval=:approval")
    Page<Blog> findBlogsByUserAndStatusAndApproval(@RequestParam("userId") Long userId, @RequestParam("status") String status, @RequestParam("approval")  String approval, Pageable pageable);

    @Query("SELECT o FROM Blog o WHERE o.status LIKE %:status% AND o.approval LIKE %:approval%")
    Page<Blog> findByStatusContainingAndApprovalContaining(@RequestParam("status") String status, @RequestParam("approval")  String approval, Pageable pageable);


    @Query("SELECT o FROM Blog o WHERE o.blogTitle LIKE %:blogTitle% AND o.status LIKE %:status% AND o.approval LIKE %:approval% " +
            "AND (:startDate IS NULL OR DATE(o.createdAt) >= :startDate) " +
            "AND (:endDate IS NULL OR DATE(o.createdAt) <= :endDate)")
    Page<Blog> findUserByTitleContainingAndStatusAndApproval(@RequestParam("blogTitle") String blogTitle, @RequestParam("status") String status, @RequestParam("approval") String approval, @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate , Pageable pageable );


    @Query("SELECT COUNT(o.blogId) FROM Blog o")
    long countAllBlog();

    @Query("SELECT COUNT(o) FROM Blog o where o.approval= :approval and MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
    long countAllBlogByMonthAndStatus(@RequestParam("approval")String approval,@RequestParam("month") int month,@RequestParam("year") int year);


}
