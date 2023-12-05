package com.DATN.FiveITViec.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.DATN.FiveITViec.model.AccessLog;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    @Query("SELECT COALESCE(COUNT(a), 0) " +
            "FROM AccessLog a " +
            "WHERE YEAR(a.timestamp) = :year " +
            "GROUP BY MONTH(a.timestamp) " +
            "ORDER BY MONTH(a.timestamp)")
    List<Long> findAccessCountByYear(@Param("year") int year);
}
