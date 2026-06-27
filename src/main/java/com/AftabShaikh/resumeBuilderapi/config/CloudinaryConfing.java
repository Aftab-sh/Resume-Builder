package com.AftabShaikh.resumeBuilderapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

/**
 * @Configuration: Ye Spring Boot ko batata hai ki ye class "Settings" ya "Configuration" ke liye hai.
 * Jab application start hogi, Spring is class ko check karega.
 */
@Configuration 
public class CloudinaryConfing // Spelling: Config (G aakhri mein)
{
    // @Value: Ye application.properties file mein se aapki API keys utha kar in variables mein daal deta hai
    @Value("${cloudinary.cloud_name}")
    private String cloudName;
    
    // ye jo name he @value me ye vhi hona chahiyye jo hamne application.property me diye he 
    @Value("${cloudinary.api_key}")
    private String apiKey;
    
    @Value("${cloudinary.api_secret}")
    private String apiSecret;
    
    /**
     * @Bean: Ye sabse important hai. Iska matlab hai ki Spring Boot "Cloudinary" ka ek object 
     * bana kar apne "Spring Container" mein rakh lega. 
     * Ab aapko pure project mein kahin bhi 'new Cloudinary()' likhne ki zarurat nahi padegi.
     */
    @Bean
    public Cloudinary cloudinary()
    {
        // ObjectUtils.asMap: Ye Cloudinary ki utility hai jo aapke credentials ko 
        // ek Map format mein convert karke Cloudinary ke constructor ko deti hai.
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
                ));
    }
} 
/*
 * “This class configures Cloudinary and creates a reusable bean using API credentials.”
 */
/*
 * Ye class kyu use ki gayi hai?

Cloudinary ko connect karne ke liye
API keys ko securely use karne ke liye
Ek central object (bean) banane ke liye

Taaki har jagah new Cloudinary() na likhna pade
 */

/*
 * Flow 
 
App start hoti hai
Spring is class ko read karta hai
@Value se credentials load hote hain
@Bean method run hota hai
Cloudinary object create hota hai
Spring container me store hota hai
Baaki classes me inject ho jata hai
 */

/*
 * Q1: @Configuration kya karta hai?
“It marks the class as a configuration class for defining beans.”

 Q2: @Bean kya karta hai?
“It creates and manages an object in Spring container.”

 Q3: @Value ka use kya hai?
“To read values from application.properties file.”

 Q4: Bean kyu banaya?
“To reuse the Cloudinary object across the application.”

 Q5: ObjectUtils.asMap kyu use kiya?
“To pass Cloudinary credentials in key-value format.”
 */

/*
 * Trick: “R-C-B”
R → Read properties
C → Create object
B → Bean register
 */

/*
 “Configuration classes are used to define beans and application settings in Spring Boot.
They help in managing dependencies, configuring external services, and centralizing application setup.”
 */

/*
 “This class is responsible for configuring Cloudinary in the application.
It reads the API credentials from properties and creates a Cloudinary bean, which is reused across services for uploading files.”
*/
