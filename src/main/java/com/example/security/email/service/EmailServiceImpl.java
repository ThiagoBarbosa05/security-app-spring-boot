package com.example.security.email.service;

import com.example.security.email.entity.EmailDetails;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
  @Autowired
  private JavaMailSender mailSender;

  @Override
  @Async
  public void sendSimpleEmail(EmailDetails emailDetails) {
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      helper.setTo(emailDetails.getRecipient());
      helper.setText(emailDetails.getMsgBody(), true);
      helper.setSubject(emailDetails.getSubject());

      mailSender.send(mimeMessage);

    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

}
