package com.AftabShaikh.resumeBuilderapi.Entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//This entity stores payment transaction details including Razorpay IDs and payment status.”
@Data 
@AllArgsConstructor
@NoArgsConstructor 
@Builder 
@Entity
public class Payment
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
 private Long id;
 private Long userId;
 private String razorpayOrderId;
 private String razorpaySignature;
 private String razorpayPaymentId;
 
private Integer amount;
private String currency;
private String planType;
@Builder.Default
private String status="created";//created , paid ,faild
private String receipt;

@CreatedDate
private LocalDateTime createdAt;
@LastModifiedDate
private LocalDateTime updatedAt;
}

/*
 * 
 * Flow 
 * 
User payment initiate karta hai
Razorpay order create hota hai
Payment complete hota hai
Payment details DB me save hoti hai
Status update hota hai (created → paid / failed)

 Important Fields ( Important)
razorpayOrderId
“Unique order ID from Razorpay.”

razorpayPaymentId
“Unique payment ID after successful transaction.”

 razorpaySignature
“Used to verify payment authenticity.”

 status
“Tracks payment state: created, paid, or failed.”

 amount & currency
“Stores payment details.”

 @CreatedDate / @LastModifiedDate
“Automatically track timestamps.”
 */

/*
 * Q1: Payment entity kyu banayi?
“To store and track payment transactions.”

Q2: Razorpay IDs ka kya use hai?
“To uniquely identify and verify transactions.”

 Important ( Must)
Q3: Payment verify kaise karte ho?
“Using Razorpay signature verification.”

Q4: Status field kyu rakha?
“To track payment lifecycle.”

 Advanced
Q5: Agar payment fail ho jaye to?
“Status ‘failed’ update karenge and handle accordingly.”

Q6: Double payment kaise avoid karoge?
“By checking orderId and status before processing.”

“Payment secure kaise kiya?”
“By verifying Razorpay signature and storing transaction details to prevent duplication or fraud.”
 */
