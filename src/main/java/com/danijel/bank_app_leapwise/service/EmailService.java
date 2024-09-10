package com.danijel.bank_app_leapwise.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${from_email}")
    private String sender;

    @Value("${to_email}")
    private String receiver;

    private JavaMailSender mailSender;

    public void sendSimpleEmail(String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(sender);

        mailSender.send(message);
    }
}