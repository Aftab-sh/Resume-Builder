package com.AftabShaikh.resumeBuilderapi.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.AftabShaikh.resumeBuilderapi.Dto.AuthResponse;
import com.AftabShaikh.resumeBuilderapi.Entity.Payment;
import com.AftabShaikh.resumeBuilderapi.Entity.User;
import com.AftabShaikh.resumeBuilderapi.Entity.UserPrincipal;
import com.AftabShaikh.resumeBuilderapi.Repository.PaymentRepository;
import com.AftabShaikh.resumeBuilderapi.Repository.UserRepository;
import com.AftabShaikh.resumeBuilderapi.Util.JwtUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public Payment createOrder(Object principal, String planType) throws RazorpayException {
        AuthResponse authResponse = authService.getProfile(principal);
        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        
        int amount = 99900; 
        String currency = "INR";
        String receipt = "premium_" + UUID.randomUUID().toString().substring(0, 8);
        
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);
        
        Order razorpayOrder = razorpayClient.orders.create(orderRequest);
        
        Payment newPayment = Payment.builder()
                .userId(authResponse.getId())
                .razorpayOrderId(razorpayOrder.get("id").toString()) 
                .amount(amount)
                .currency(currency)
                .planType(planType)
                .status("created")
                .receipt(receipt)
                .build();
      
        return paymentRepository.save(newPayment);
    }

    public Map<String, Object> verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);
            
            boolean isValidSignature = Utils.verifyPaymentSignature(attributes, razorpayKeySecret);
            
            if (isValidSignature) {
                Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                        .orElseThrow(() -> new RuntimeException("Payment record not found"));

                payment.setRazorpayPaymentId(razorpayPaymentId);
                payment.setRazorpaySignature(razorpaySignature);
                payment.setStatus("paid");
                paymentRepository.save(payment);
                
                User user = upgradeUserSubscription(payment.getUserId(), payment.getPlanType());
                
                // ✅ Naya token with updated plan
                UserPrincipal userPrincipal = new UserPrincipal(user);
                String freshToken = jwtUtil.generateToken(userPrincipal);
                
                return Map.of(
                    "success", true,
                    "message", "Payment verified! Subscription upgraded.",
                    "token", freshToken,
                    "subscriptionPlan", user.getSubscriptionPlan()
                );
            }
            return Map.of("success", false, "message", "Invalid signature");
        } catch (Exception e) {
            log.error("Payment verification failed", e);
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    private User upgradeUserSubscription(Long userId, String planType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setSubscriptionPlan(planType.toLowerCase());
        return userRepository.save(user);
    }

    public List<Payment> getUserPayments(Object principal) {
        AuthResponse authResponse = authService.getProfile(principal);
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(authResponse.getId());
    }

    public Payment getPaymentDetails(String orderId) {
        return paymentRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}