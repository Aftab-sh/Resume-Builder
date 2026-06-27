package com.AftabShaikh.resumeBuilderapi.Controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AftabShaikh.resumeBuilderapi.Entity.Payment;
import com.AftabShaikh.resumeBuilderapi.Service.PaymentService;
import com.AftabShaikh.resumeBuilderapi.Util.JwtUtil;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@Slf4j
public class PaymentController

{
	
private final PaymentService paymentService;





@PostMapping("/create-order")
public ResponseEntity<?> createdOrder(@RequestBody Map<String,String> request,
                                       Authentication autentication) throws RazorpayException
{
	// step 1: validate the request
	String planType = request.get("planType");
	if(!"premium".equalsIgnoreCase(planType))
	{
		return ResponseEntity.badRequest().body(Map.of("message","Invalid plan type"));
	}
	//Step 2: call the service method 
	Payment payment= paymentService.createOrder(autentication.getPrincipal(),planType);
	//Step 3: Prepare the response object
	Map<String,Object> response =Map.of(
			        "orderId",payment.getRazorpayOrderId(),
			        "amount",payment.getAmount(),
			        "currency",payment.getCurrency(),
			        "receipt",payment.getReceipt()
			        );
	//step 4: return the response
	return ResponseEntity.ok(response);
	 
	
}
@PostMapping("/verify")
public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> request) {
    String razorpayOrderId = request.get("razorpay_order_id");
    String razorpayPaymentId = request.get("razorpay_payment_id");
    String razorpaySignature = request.get("razorpay_signature");

    if (Objects.isNull(razorpayOrderId) || Objects.isNull(razorpaySignature) || Objects.isNull(razorpayPaymentId)) {
        return ResponseEntity.badRequest().body(Map.of("message", "Missing required parameters"));
    }

    Map<String, Object> result = paymentService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature);
    
    if ((boolean) result.get("success")) {
        return ResponseEntity.ok(result);
    }
    return ResponseEntity.badRequest().body(result);
}
@GetMapping("/history")
public ResponseEntity<?> getPaymentHistory(Authentication authentication)
{
	//step 1: call the service
	List<Payment> payments=paymentService.getUserPayments(authentication.getPrincipal());
	
	//step 2: return the response
			return ResponseEntity.ok(payments);
	
}


@GetMapping("/order/{orderId}")
public ResponseEntity<?> getOrderDetails(@PathVariable String orderId)
{
	//step 1: call the service method 
	Payment paymentDetails=paymentService.getPaymentDetails(orderId);
	
	//step 2: return response
	return ResponseEntity.ok(paymentDetails);
	
}

}
