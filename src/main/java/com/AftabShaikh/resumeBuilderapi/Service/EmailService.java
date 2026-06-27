package com.AftabShaikh.resumeBuilderapi.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;

/*
 * “This class handles email sending using Spring’s JavaMailSender with 
  support for HTML content and attachments.”
 */
@Slf4j
@Service
public class EmailService {
	


    // Application.properties se sender email utha raha hai
    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    // Email bhejne ke liye Spring ka main tool
    //agar ham yaha null assign kr dete to java nullpointer exception de deta 
    private final JavaMailSender mailSender;
    

    // MANUAL CONSTRUCTOR: Ye zaroori hai JavaMailSender ko initialize karne ke liye
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Ye function HTML email bhejta hai.
     * Ise hum AuthService.java se call karenge.
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        
    	log.info("Inside EmailService - sendHtmlEmail():{},{},{}",to,subject,htmlContent);
        // MimeMessage HTML content support karta hai
        MimeMessage message = mailSender.createMimeMessage();
        
        // Helper class jo message set karne mein asani deti hai
        // "true" ka matlab hai ki hum attachments aur HTML bhej sakte hain
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true = HTML format

        // Final step: Email bhej dena
        log.info("Email is send");
        mailSender.send(message);
    }
    
    public void sendEmailWitAttachmetnt(String to,String subject,String body,byte[] attachment,String filename) throws MessagingException
    {
    	MimeMessage message = mailSender.createMimeMessage();
    	MimeMessageHelper helper= new MimeMessageHelper(message,true);
    	helper.setFrom(fromEmail);
    	helper.setTo(to);
    	helper.setSubject(subject);
    	helper.setText(body);
    	helper.addAttachment(filename, new ByteArrayResource(attachment));
    	mailSender.send(message);
    }
}

/*
 * Ye class kyu use ki gayi hai?

 Use cases:

Email verification
Password reset
Notifications
Resume send karna (tumhare project me )
*/

/*
 * sendHtmlEmail()
“Used to send emails with HTML content.”

 sendEmailWithAttachment()
“Used to send emails with file attachments like PDF or images.”
 */

/*
 * Q1: JavaMailSender kya hai?
“It is a Spring interface used to send emails.”

Q2: MimeMessage kya hota hai?
“It is used to create rich email content like HTML and attachments.”

Q3: MimeMessageHelper kyu use kiya?
“To simplify setting email properties like subject, body, and attachments.”

Q4: @Service annotation kyu use kiya?
“To mark this class as a service component managed by Spring.”

Q5: HTML email kaise send karte ho?
“By setting helper.setText(content, true).”
 */

/*
 * “This service is responsible for sending emails using JavaMailSender.
It supports sending both HTML emails and emails with attachments.
It uses MimeMessage and MimeMessageHelper to construct and send the email.”
 */
