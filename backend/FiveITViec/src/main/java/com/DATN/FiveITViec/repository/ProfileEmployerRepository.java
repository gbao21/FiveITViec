package com.DATN.FiveITViec.repository;

import com.DATN.FiveITViec.model.ProfileEmployer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileEmployerRepository extends JpaRepository<ProfileEmployer,Long> {
    @Query("SELECT o FROM ProfileEmployer o WHERE o.user.userId= :userId")
    ProfileEmployer findProfileEmployerByUserId(@Param("userId")Long userId);
}
