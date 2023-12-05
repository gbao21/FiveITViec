package com.DATN.FiveITViec.repository;

import com.DATN.FiveITViec.model.ProfileCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileCandidateRepository extends JpaRepository<ProfileCandidate,Long> {
    @Query("SELECT o FROM ProfileCandidate o WHERE o.user.userId= :userId")
    ProfileCandidate findProfileCandidateByUserId(@Param("userId")Long userId);
}
