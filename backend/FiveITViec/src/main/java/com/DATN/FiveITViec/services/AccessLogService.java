package com.DATN.FiveITViec.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DATN.FiveITViec.model.AccessLog;
import com.DATN.FiveITViec.repository.AccessLogRepository;

@Service
public class AccessLogService {
    @Autowired
    private AccessLogRepository accessLogRepository;

    public List<Long> getAccessCountByYear(int year) {
        List<Long> accessCounts = accessLogRepository.findAccessCountByYear(year);

        accessCounts.addAll(Collections.nCopies(12 - accessCounts.size(), 0L));
        Collections.reverse(accessCounts);
        return accessCounts;
    }

    public void logAccess() {
        AccessLog accessLog = new AccessLog();
        accessLogRepository.save(accessLog);
    }
}
