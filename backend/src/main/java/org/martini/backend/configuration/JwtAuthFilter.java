package org.martini.backend.configuration;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.martini.backend.service.JwtService;
import org.martini.backend.service.SimpleUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {


  private final JwtService jwtService;
  private final SimpleUserDetailsService simpleUserDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String token = null;
    // Get token from Authorization header (e.g., "Bearer <token>")
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7); // remove "Bearer " prefix
    }

    if (token == null) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      if (SecurityContextHolder.getContext().getAuthentication() == null) {
        String username = jwtService.getUsername(token);
        UserDetails userDetails = simpleUserDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
      filterChain.doFilter(request, response);
    } catch (TokenExpiredException e) {
      log.warn("JWT token expired");
      filterChain.doFilter(request, response);
    } catch (JWTVerificationException e) {
      log.warn("There was an issue while validating jwt: {}", e.getMessage());
      filterChain.doFilter(request, response);
    }

  }
}
