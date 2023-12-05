package com.DATN.FiveITViec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.DATN.FiveITViec.model.ProfileAdmin;

@Repository
public interface ProfileAdminRepository extends JpaRepository<ProfileAdmin, Long> {
    @Query("SELECT o FROM ProfileAdmin o WHERE o.user.userId= :userId")
    ProfileAdmin findProfileAdminByUserId(@Param("userId") Long userId);
}
