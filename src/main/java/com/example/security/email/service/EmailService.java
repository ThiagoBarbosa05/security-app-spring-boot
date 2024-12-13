package com.example.security.email.service;

import com.example.security.email.entity.EmailDetails;

public interface EmailService {
  void sendSimpleEmail(EmailDetails emailDetails);
}
