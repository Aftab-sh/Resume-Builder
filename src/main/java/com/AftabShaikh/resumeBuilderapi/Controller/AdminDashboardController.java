package com.AftabShaikh.resumeBuilderapi.Controller;

import com.AftabShaikh.resumeBuilderapi.Entity.User;
import com.AftabShaikh.resumeBuilderapi.Entity.Payment;
import com.AftabShaikh.resumeBuilderapi.Service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // 🔒 Strict Admin Check Enforced Globally
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    // 🚀 1. GET ALL SYSTEM STATS PANEL
    // End Point: GET http://localhost:8080/api/admin/dashboard/stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        return ResponseEntity.ok(adminDashboardService.getDashboardStats());
    }

    // 🚀 2. GET ALL REGISTERED USERS FOR MANAGEMENT TABLE
    // End Point: GET http://localhost:8080/api/admin/dashboard/users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminDashboardService.getAllUsers());
    }

    // 🚀 3. UPDATE USER SUBSCRIPTION PLAN MANUALLY
    // End Point: PUT http://localhost:8080/api/admin/dashboard/users/{id}/plan?plan=premium
    @PutMapping("/users/{id}/plan")
    public ResponseEntity<?> updateUserPlan(@PathVariable Long id, @RequestParam String plan) {
        try {
            User updatedUser = adminDashboardService.updateUserPlan(id, plan);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User subscription override successful!",
                "user", updatedUser
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // 🚀 4. GET GLOBAL RAZORPAY TRANSACTION LOGS
    // End Point: GET http://localhost:8080/api/admin/dashboard/payments
    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPaymentsHistory() {
        return ResponseEntity.ok(adminDashboardService.getAllPaymentLogs());
    }
}