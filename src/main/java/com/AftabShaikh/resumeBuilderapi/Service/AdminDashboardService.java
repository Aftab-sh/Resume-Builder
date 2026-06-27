package com.AftabShaikh.resumeBuilderapi.Service;

import com.AftabShaikh.resumeBuilderapi.Entity.User;
import com.AftabShaikh.resumeBuilderapi.Entity.Payment;
import com.AftabShaikh.resumeBuilderapi.Repository.UserRepository;
import com.AftabShaikh.resumeBuilderapi.Repository.TemplateRepository;
import com.AftabShaikh.resumeBuilderapi.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final PaymentRepository paymentRepository;

    // 📊 1. SYSTEM METRICS/STATS: Analytics panel ke liye dynamic data
    public Map<String, Object> getDashboardStats() {
        log.info("Fetching global admin system stats metrics");
        
        long totalUsers = userRepository.count();
        long totalTemplates = templateRepository.count();
        
        // Database ke users list se dynamic filter counters (Lowercase logic mapped)
        List<User> users = userRepository.findAll();
        long premiumUsersCount = users.stream().filter(u -> "premium".equalsIgnoreCase(u.getSubscriptionPlan())).count();
        long basicUsersCount = users.stream().filter(u -> "basic".equalsIgnoreCase(u.getSubscriptionPlan()) || u.getSubscriptionPlan() == null).count();

        // Total Paid transactions se active income revenue generate karna
        List<Payment> payments = paymentRepository.findAll();
        long totalRevenue = payments.stream()
                .filter(p -> "paid".equalsIgnoreCase(p.getStatus()))
                .mapToLong(Payment::getAmount)
                .sum() / 100; // Razorpay paisa/paise me store hota hai, use INR me convert kiya

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("premiumUsers", premiumUsersCount);
        stats.put("basicUsers", basicUsersCount);
        stats.put("totalTemplates", totalTemplates);
        stats.put("totalRevenueINR", totalRevenue);

        return stats;
    }

    // 👥 2. USER MANAGEMENT: Saare registered users ko manage karna
    public List<User> getAllUsers() {
        log.info("Fetching all registered system users for User Management Panel");
        return userRepository.findAll();
    }

    // 🔄 User Plan manually update/override karna (Basic <-> Premium)
    public User updateUserPlan(Long userId, String newPlan) {
        log.info("Admin modifying plan for user ID: {} to {}", userId, newPlan);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        user.setSubscriptionPlan(newPlan.toLowerCase()); // Lowercase matching fix
        return userRepository.save(user);
    }

    // 💳 3. PAYMENT LOGS: Razorpay transactions track karne ke liye
    public List<Payment> getAllPaymentLogs() {
        log.info("Fetching global order transaction logs history for financial monitoring");
        return paymentRepository.findAllByOrderByCreatedAtDesc();
    }
}