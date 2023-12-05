package com.DATN.FiveITViec.CommonController.AdminController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.DATN.FiveITViec.services.AccessLogService;

@RestController
@RequestMapping("/auth/admin")
public class AccessLogController {
    @Autowired
    private AccessLogService accessLogService;

    @GetMapping("/access-count/{year}")
    public List<Long> getAccessCountByYear(@PathVariable int year) {
        return accessLogService.getAccessCountByYear(year);
    }

    @PostMapping("/log")
    public void logAccess() {
        accessLogService.logAccess();
    }
}
