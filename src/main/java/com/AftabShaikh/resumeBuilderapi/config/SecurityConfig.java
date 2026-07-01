package com.AftabShaikh.resumeBuilderapi.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource; // Sahi import (MVC wala)
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Sahi import

import com.AftabShaikh.resumeBuilderapi.Entity.Permission;
import com.AftabShaikh.resumeBuilderapi.Security.JwtAuthenticationEntryPoint;
import com.AftabShaikh.resumeBuilderapi.Security.JwtAuthenticationFilter;
import com.AftabShaikh.resumeBuilderapi.Service.CustomUserDetailsService;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*
 * “This class configures security rules, enables JWT authentication, and makes the application stateless.”
 */

@Configuration // Spring ko batata hai ki ye settings file hai
@EnableWebSecurity // Security rules ko active karta hai
@EnableMethodSecurity
public class SecurityConfig 
{
	//to register this
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService; // 👈 Inject service directly

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter 
			            ,CustomUserDetailsService customUserDetailsService)
	{
	    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	    this.customUserDetailsService=customUserDetailsService;
	}

    // 1. Password ko Hash (Encrypt) karne ke liye BCrypt algorithm use kar rahe hain
    @Bean
    public PasswordEncoder passwordEncoder() 
    {
        return new BCryptPasswordEncoder();
    }

    // 2. HTTP Security rules define kar rahe hain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
            // CORS settings apply kar rahe hain (React connection ke liye)
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) 
            
            // CSRF ko disable kar rahe hain kyunki hum Stateless JWT use kar rahe hain
            //csrf needed web application   
            .csrf(csrf -> csrf.disable()) 
            
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers("/api/auth/register", "/api/auth/login", 
            	                     "/api/auth/verify-email","/api/auth/upload-image",
            	                     "/api/auth/resend-verification",
            	                      "/actuator/**").permitAll()
            	    
            	    
          
            	 // 2. Resume APIs (Sirf vahi Users access kar payenge jinke paas specific permissions hain)
            	    .requestMatchers(HttpMethod.GET, "/api/resumes/**").hasAuthority("USER_RESUME_READ")
            	    .requestMatchers(HttpMethod.POST, "/api/resumes/**").hasAuthority("USER_RESUME_WRITE")
            	    .requestMatchers(HttpMethod.PUT, "/api/resumes/**").hasAuthority("USER_RESUME_UPDATE")
            	    .requestMatchers(HttpMethod.DELETE, "/api/resumes/**").hasAuthority("USER_RESUME_DELETE")
            	    
            	    // 3. Payment APIs (Sirf login user jo payment trigger kar sake)
            	    .requestMatchers("/api/payment/create-order").hasAuthority("USER_PAY_PAYMENT")
            	    .requestMatchers("/api/payment/verify").authenticated() // Verification koi bhi logged-in user hit kar sake
            	    .requestMatchers("/api/payment/history").authenticated()
            	    
            	    // 4. Templates APIs
            	    .requestMatchers("/api/templates/**").authenticated()
            	    
            	    
            	    
            	    
            	    
            	    .anyRequest().authenticated()
            	)
            // SABSE IMPORTANT: Session ko STATELESS rakhna (Server par session save nahi hoga, sirf JWT chalega)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Humara Custom JWT Filter: Har request Controller tak pahunchne se pehle isse guzregi
            // Ise 'UsernamePasswordAuthenticationFilter' se pehle chala rahe hain
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            
            .exceptionHandling(ex ->ex.authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        

        return http.build();
        
    }
    


    // 3. CORS Configuration: React (Localhost 5173) se backend ko connect karne ke liye
    @Bean
    public CorsConfigurationSource corsConfigurationSource() 
    {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // DONO ports ko ek saath allow karo (5173 aur 5174)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174"));
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // Ye line bahut zaruri hai headers allow karne ke liye
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
    
  

@Bean
public AuthenticationManager authenticationManager()
{
	
	DaoAuthenticationProvider daoAuthProvider=new DaoAuthenticationProvider();
	daoAuthProvider.setUserDetailsService(customUserDetailsService);
	daoAuthProvider.setPasswordEncoder(passwordEncoder());
	return new ProviderManager(daoAuthProvider);
	
   }
 }

