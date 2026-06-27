package com.AftabShaikh.resumeBuilderapi.Security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.AftabShaikh.resumeBuilderapi.Entity.User;
import com.AftabShaikh.resumeBuilderapi.Entity.UserPrincipal;
import com.AftabShaikh.resumeBuilderapi.Repository.UserRepository;
import com.AftabShaikh.resumeBuilderapi.Util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userEmail = null; // 🚀 Name changed from userId to userEmail for clarity

        // 1. Extract token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                // Kyunki token se actual me email nikal raha hai
                userEmail = jwtUtil.getUserIdFromToken(token); 
            } catch (Exception e) {
                log.error("Token verification failed: {}", e.getMessage());
            }
        }

        // 2. If email exists and no authentication yet
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // 3. Validate token
                if (jwtUtil.validateToken(token) && !jwtUtil.isTokenExpired(token)) {
                    
                	// 4. Load user using findByEmail directly
                	User user = userRepository.findByEmail(userEmail);

                	if (user == null) {
                	    throw new UsernameNotFoundException("User not found with email: " + userEmail);
                	}

                    // 5. Load authorities
                    UserPrincipal userPrincipal = new UserPrincipal(user);
                    Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();

                    // 6. Create authentication token 
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, authorities);

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 7. Set authentication
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.info("Authenticated user: {} with role: {}", user.getEmail(), user.getRole());
                }
            } catch (Exception e) {
                log.error("Could not set authentication: {}", e.getMessage());
            }
        }

        // 8. Continue call to next filter in filter chain 
        filterChain.doFilter(request, response);
    }
}