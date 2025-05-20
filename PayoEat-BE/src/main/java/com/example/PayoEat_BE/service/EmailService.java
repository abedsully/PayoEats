package com.example.PayoEat_BE.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String token) {
        String subject = "Confirm Your Registration - Payoeat";
        String confirmationUrl = "http://localhost:8080/api/auth/confirm?token=" + token;

        String message = """
                Dear User,

                Thank you for registering with Payoeat!

                Please confirm your email address by clicking the link below:
                %s

                If you did not register for Payoeat, please ignore this email.

                Best regards,
                The Payoeat Team

                ---
                This is an automated message, please do not reply to this email.
                """.formatted(confirmationUrl);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}