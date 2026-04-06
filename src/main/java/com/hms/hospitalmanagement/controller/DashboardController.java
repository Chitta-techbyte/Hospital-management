package com.hms.hospitalmanagement.controller;

import com.hms.hospitalmanagement.dto.DashboardDTO;
import com.hms.hospitalmanagement.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // 🔥 ADMIN DASHBOARD
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public DashboardDTO getAdminDashboard() {
        return dashboardService.getAdminDashboard();
    }
}