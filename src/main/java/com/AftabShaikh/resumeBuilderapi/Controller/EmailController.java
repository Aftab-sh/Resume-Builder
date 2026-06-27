package com.AftabShaikh.resumeBuilderapi.Controller;

import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.AftabShaikh.resumeBuilderapi.Service.EmailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
@Slf4j
public class EmailController
{
private final EmailService emailService;

@PostMapping(value="/send-resume",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<Map<String,Object>> sendResumeByEmail( 
		 @RequestPart("recipientEmail")String recipientEmail,
		 @RequestPart("subject")String subject,
		 @RequestPart("message")String message,
		 @RequestPart("pdfFile")MultipartFile pdfFile,
		 Authentication authentication
		 ) throws IOException, MessagingException
{
	  log.info("send Email controller is called ");
	//Step 1: Validate the inputs 
	Map<String,Object> response=new HashMap<>();
	if(Objects.isNull(recipientEmail) || Objects.isNull(pdfFile))
	{
		response.put("succes", false);
		response.put("message", "Missing required fields");
		return ResponseEntity.badRequest().body(response);
	}
			
				
		
	
	//Step 2: Get the file data 
	byte[] pdfBytes=pdfFile.getBytes();
	String originalFilename=pdfFile.getOriginalFilename();
	String filename=Objects.nonNull(originalFilename) ? originalFilename:"resume.pdf";
	
	//Step 3: prepare the email content
	
	String emailSubject= Objects.nonNull(subject) ? subject : "Resume Application";
	String emailBody = Objects.nonNull(message) ? message:"Please find my resume attached.\n\n Best Regards";
	
	//Step 4: call the service method to send the email with an attechment
	emailService.sendEmailWitAttachmetnt(recipientEmail,emailSubject,emailBody,pdfBytes,filename);
	
	//Step 5:return response
      response.put("success", true);
      response.put("message", "Resume send Successfully to"+recipientEmail);
      return ResponseEntity.ok(response);
	
}



}
