package com.AftabShaikh.resumeBuilderapi.Repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AftabShaikh.resumeBuilderapi.Entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long>
{
	Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
	Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);
	List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);
	List<Payment> findByStatus(String status);
	

    
    // 🚀 Admin Panel ke transaction history ke liye
    List<Payment> findAllByOrderByCreatedAtDesc();
	

}
/*
 *Ye class kyu use ki gayi hai?

 Purpose:

Payment data DB se fetch karna
Razorpay IDs ke basis par search karna
User ke payments list nikalna 
 */
 
