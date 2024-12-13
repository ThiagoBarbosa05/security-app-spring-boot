package com.example.security.auth.filter;

import com.example.security.auth.UserDetailsImpl;
import com.example.security.exceptions.UserNotFoundException;
import com.example.security.providers.JwtProvider;

import com.example.security.user.model.User;
import com.example.security.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtProvider jwtProvider;

  @Autowired
  private UserRepository userRepository;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {

    try {
      String token = extractToken(request);
      if (token != null && jwtProvider.isTokenValid(token)) {
        authenticateUser(token, request);
      }
    } catch (Exception ex) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Token inválido ou expirado");
      return;
    }

      filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");

    if(authorizationHeader != null &&  authorizationHeader.startsWith("Bearer ")) {
      return authorizationHeader.replace("Bearer ", "");
    }
    return null;
  }

  private void authenticateUser(String token, HttpServletRequest request) {
    String email = jwtProvider.getSubjectFromToken(token);
    User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

    UserDetailsImpl userDetails = new UserDetailsImpl(user);

    Authentication authentication = new UsernamePasswordAuthenticationToken(
      userDetails,
      null,
      userDetails.getAuthorities()
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    request.setAttribute("user", user);
  }

}
