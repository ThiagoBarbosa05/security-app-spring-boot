package com.example.security.auth.oauth2;

import com.example.security.providers.JwtProvider;
import com.example.security.providers.JwtProviderImpl;
import com.example.security.providers.dto.JwtPayload;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

  @Autowired
  private JwtProvider jwtProvider;

  @Value("${base.url}")
  private String baseUrl;

  @Override
  public void onAuthenticationSuccess(
    HttpServletRequest request,
    HttpServletResponse response,
    Authentication authentication
  ) throws IOException, ServletException {

    OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;

    DefaultOidcUser oidcUser = (DefaultOidcUser) authenticationToken.getPrincipal();
    JwtPayload jwtPayload = new JwtPayload(oidcUser.getEmail());

    String accessToken = jwtProvider.generateToken(jwtPayload);

    response.sendRedirect(baseUrl + "/auth/social?token=" + accessToken);
  }

}
