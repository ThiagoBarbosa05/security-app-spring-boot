package com.example.security.user.service;

import com.example.security.auth.UserDetailsImpl;
import com.example.security.email.entity.EmailDetails;
import com.example.security.email.service.EmailService;
import com.example.security.exceptions.*;
import com.example.security.providers.JwtProvider;
import com.example.security.providers.dto.JwtPayload;
import com.example.security.user.dto.*;
import com.example.security.user.model.*;
import com.example.security.user.repository.TokenRepository;
import com.example.security.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

  @Value("${base.url}")
  private String baseUrl;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private EmailService emailService;

  @Autowired
  private TokenRepository tokenRepository;

  @Autowired
  private JwtProvider jwtProvider;

  @Autowired
  private SpringTemplateEngine springTemplateEngine;

  @Override
  public User registerNewUser(RegisterUserRequest registerUserRequest) {

    boolean userAlreadyExists = userRepository.existsByEmail(
      registerUserRequest.getEmail()
    );

    if(userAlreadyExists) {
      throw new UserAlreadyExistsException();
    }

    String hashedPassword = passwordEncoder.encode(
      registerUserRequest.getPassword()
    );

    User user = User.builder()
      .firstName(registerUserRequest.getFirstName())
      .lastName(registerUserRequest.getLastName())
      .password(hashedPassword)
      .email(registerUserRequest.getEmail())
      .roles(List.of(Role.builder().name(RoleName.ROLE_USER).build())).build();


    return userRepository.save(user);
  }

  @Override
  public String authenticateUser(LoginUserRequest loginUserRequest) {

    try {
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(loginUserRequest.email(), loginUserRequest.password());

      Authentication authentication = authenticationManager.authenticate(
        usernamePasswordAuthenticationToken
      );

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      JwtPayload jwtPayload = new JwtPayload(userDetails.getUsername());

      return jwtProvider.generateToken(jwtPayload);

    } catch (BadCredentialsException exception) {
      throw new InvalidCredentialsException();
    }
  }

  @Override
  public User getUserByEmail(GetUserByEmailRequest getUserByEmailRequest) {
    return userRepository.findByEmail(getUserByEmailRequest.email())
      .orElseThrow(UserNotFoundException::new);
  }

  @Override
  @Transactional
  public void sendEmailToResetPassword(ResetPasswordRequest resetPasswordRequest) {
    User user = userRepository.findByEmail(resetPasswordRequest.email())
                                .orElseThrow(UserNotFoundException::new);

    Token token = Token.builder()
      .type(TokenType.RECOVERY_PASSWORD)
      .expiresAt(LocalDateTime.now().plusMinutes(15))
      .user(user).build();

    Token savedToken = tokenRepository.save(token);

    String resetLink = baseUrl + "/auth/update/password?token=" + savedToken.getId();
    Context thymeleafContext = new Context();
    thymeleafContext.setVariable("user", user);
    thymeleafContext.setVariable("link", resetLink);
    String body = springTemplateEngine.process("reset-password", thymeleafContext);

    EmailDetails emailDetails = EmailDetails.builder()
      .msgBody(body)
      .recipient(user.getEmail())
      .subject("Recuperação de senha").build();

    emailService.sendSimpleEmail(emailDetails);
  }

  @Override
  @Transactional
  public void updateUserPassword(UpdateUserPasswordRequest updateUserPasswordRequest) {
    Token token = tokenRepository.findById(updateUserPasswordRequest.getToken())
      .orElseThrow(TokenNotFoundException::new);

    if(token.isExpired()) {
      throw new ResetPasswordTokenExpiredException();
    }

    User user = token.getUser();

    String newPasswordHashed = passwordEncoder.encode(updateUserPasswordRequest.getPassword());

    user.setPassword(newPasswordHashed);

    userRepository.save(user);

    tokenRepository.deleteAllByUserId(user.getId());

  }

  @Override
  public void updateUser(UpdateUserRequest updateUserRequest, User user) {
    User userExisting = userRepository.findByEmail(
      user.getEmail()
    ).orElseThrow(UserNotFoundException::new);


    userExisting.setFirstName(updateUserRequest.firstName());
    userExisting.setLastName(updateUserRequest.lastName());

    userRepository.save(userExisting);
  }

  @Override
  public void deleteUser(UUID userId) {
    userRepository.deleteById(userId);
  }

  @Override
  public void updateProfileImage(UpdateImageProfileRequest updateImageProfileRequest) {
      User user = userRepository.findById(
        updateImageProfileRequest.userId()
      ).orElseThrow(UserNotFoundException::new);

      user.setAvatar(updateImageProfileRequest.key());

      userRepository.save(user);
  }

}
