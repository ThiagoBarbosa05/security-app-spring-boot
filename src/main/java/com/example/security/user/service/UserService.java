package com.example.security.user.service;

import com.example.security.user.dto.*;
import com.example.security.user.model.Token;
import com.example.security.user.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
  User registerNewUser(RegisterUserRequest registerUserRequest);
  String authenticateUser(LoginUserRequest loginUserRequest);
  User getUserByEmail(GetUserByEmailRequest getUserByEmailRequest);
  void sendEmailToResetPassword(ResetPasswordRequest resetPasswordRequest);
  void updateUserPassword(UpdateUserPasswordRequest updateUserPasswordRequest);
  void updateUser(UpdateUserRequest updateUserRequest, User user);
  void deleteUser(UUID userId);
  void updateProfileImage(UpdateImageProfileRequest updateImageProfileRequest);
}
