package com.DATN.FiveITViec.repository;

import com.DATN.FiveITViec.model.Job;
import com.DATN.FiveITViec.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

        User findByEmail(String email);

        @Query("SELECT o FROM User o WHERE o.roles.roleName LIKE %:roleName% AND o.email LIKE %:email% AND o.approval LIKE %:approval% " +
                "AND (:startDate IS NULL OR DATE(o.createdAt) >= :startDate) " +
                "AND (:endDate IS NULL OR DATE(o.createdAt) <= :endDate)" +
                "AND o.status LIKE %:status% ")
        Page<User> findUserByRolesAndEmailAndApprovalContaining(@RequestParam("roleName") String roleName, @RequestParam("email") String email, @RequestParam("approval") String approval, @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate, @RequestParam("status") String status , Pageable pageable );

        @Query("SELECT COUNT(o.userId) FROM User o WHERE YEAR(o.createdAt) = :year AND o.roles.roleId = (SELECT r.roleId FROM Roles r WHERE r.roleName LIKE %:roleName%) AND (CASE WHEN :month IS NULL THEN TRUE ELSE MONTH(o.createdAt) = :month END) = TRUE")
        long countUserByRole(@RequestParam("year") Integer year, @RequestParam("month") Integer month, @RequestParam("roleName") String roleName);
        
        @Query("SELECT u FROM User u WHERE u.userId IN (SELECT pe.user.userId FROM ProfileEmployer pe WHERE pe.companyName LIKE %:companyName% AND pe.address LIKE %:address%) " +
                "AND u.roles.roleName = 'employer'" +
                "AND u.approval ='approved' " +
                "AND u.status = 'enable'")
        Page<User> findEmployersWithCompanyName(
                @RequestParam(value = "companyName", required = false) String companyName,@RequestParam(value = "address", required = false) String address,Pageable pageable);

        @Query("SELECT COUNT(o.userId) FROM User o where o.roles.roleName= :roleName")
        long countUserByRole(@RequestParam("roleName") String roleName);

        @Query("SELECT COUNT(o) FROM User o WHERE o.roles.roleName= :roleName AND MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
        Long countAllCandidatesByMonth(@RequestParam("roleName") String roleName,@RequestParam("month") int month, @RequestParam("year") int year);

        @Query("SELECT o FROM User o where o.roles.roleName = 'employer' ORDER BY o.createdAt DESC")
        List<User> findLatestRecordsWithLimit();

        @Query("SELECT COUNT(o) FROM User o WHERE o.roles.roleName= :roleName AND o.approval= :approval and MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
        Long countAllEmployerByMonthAndStatus(@RequestParam("roleName") String roleName,@RequestParam("approval") String approval,@RequestParam("month") int month, @RequestParam("year") int year);

}
