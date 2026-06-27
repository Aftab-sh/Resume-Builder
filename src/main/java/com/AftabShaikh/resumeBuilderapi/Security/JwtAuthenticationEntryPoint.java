package com.AftabShaikh.resumeBuilderapi.Security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper; // Sahi import Jackson library ke liye

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * hamne security config me ye line likhi he 
 * .exceptionHandling(ex -> ex.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
Is line se Spring Security ko pata chal jata hai ki "agar authentication fail ho to ye class call karna".
 
*/

/**
 * Ye class tab chalti hai jab koi unauthenticated user kisi 
 * "Protected API" ko access karne ki koshish karta hai.
 */
//configur this into security config 
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint
{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        // 1. Response ka type JSON set kar rahe hain
        response.setContentType("application/json");
        
        // 2. HTTP Status 401 (Unauthorized) bhej rahe hain
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        // 3. Ek clean error message taiyar kar rahe hain
        Map<String, String> errorResponse = new HashMap<>(); // Syntax sahi kiya
        errorResponse.put("message", "Full authentication is required to access this resource");
        errorResponse.put("error", "Unauthorized");

        // 4. ObjectMapper ka use karke Java Map ko JSON mein badal kar response stream mein bhej rahe hain
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
/*
 * “This class is used to handle unauthorized requests and send a proper 401 error response in JSON format.”
*/


   /*
Flow -
)
User request bhejta hai
Authentication fail ho jata hai
Spring Security is class ko call karta hai
Ye 401 Unauthorized response bhejti hai
JSON me error message return karti hai
*/

/*
 * Ye class kyu use ki gayi hai?

Simple language me:

Jab user login nahi hai
Ya invalid token bhejta hai
Aur protected API call karta hai

 Tab:

Ye class automatically run hoti hai
Aur custom error response bhejti hai
 */

/*
Q1: AuthenticationEntryPoint kya hota hai?
“It is used to handle unauthorized access attempts in Spring Security.”

 Q2: Ye class kab call hoti hai?
“When an unauthenticated user tries to access a secured API.”

 Q3: 401 status code kya hota hai?
“It means Unauthorized — authentication is required.”

 Q4: ObjectMapper kyu use kiya?
“To convert Java object (Map) into JSON response.”

 Q5: Default behavior kya hota agar ye class na ho?
“Spring Security would return a default error page instead of a custom JSON response.”
*/