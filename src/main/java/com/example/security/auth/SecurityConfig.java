package com.example.security.auth;

import com.example.security.auth.filter.SecurityAuthenticationFilter;
import com.example.security.auth.oauth2.CustomOAuth2FailureHandler;
import com.example.security.auth.oauth2.CustomOAuth2SuccessHandler;
import com.example.security.auth.oauth2.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${base.url}")
  private String baseUrl;

  @Autowired
  private SecurityAuthenticationFilter securityAuthenticationFilter;

  @Autowired
  private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

  @Autowired
  private CustomOAuth2UserService customOAuth2UserService;

  @Autowired
  private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Autowired
  private CustomOAuth2FailureHandler customOAuth2FailureHandler;

  private static final String[] SWAGGER_LIST = {
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/swagger-resource/**",
  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


    httpSecurity
      .csrf(AbstractHttpConfigurer::disable)
      .cors(Customizer.withDefaults())
      .authorizeHttpRequests(request -> request
        .requestMatchers("/api/users/registration", "/api/users/authenticate").permitAll()
        .requestMatchers("/api/users/upload/avatar").permitAll()
        .requestMatchers("/api/users/recovery-password").permitAll()
        .requestMatchers("/api/users/update-password").permitAll()
        .requestMatchers("/", "/login", "/oauth2/**").permitAll()
        .requestMatchers(SWAGGER_LIST).permitAll()
        .anyRequest().authenticated()
      )
      .oauth2Login(oauth2 -> oauth2
        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
        .successHandler(customOAuth2SuccessHandler)
        .failureHandler(customOAuth2FailureHandler)
      )
      .addFilterBefore(securityAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .exceptionHandling(customizer -> {
        customizer.authenticationEntryPoint(
         customAuthenticationEntryPoint
        );
      })
      .logout(config -> config
        .clearAuthentication(true)
        .deleteCookies("JSESSIONID")
        .invalidateHttpSession(true)
        .logoutSuccessUrl(baseUrl)
      );


      return httpSecurity.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
