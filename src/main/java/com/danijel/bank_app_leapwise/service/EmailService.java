package com.danijel.bank_app_leapwise.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Value("${from_email}")
    private String sender;

    @Value("${to_email}")
    private String receiver;

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String subject, String body) {

        log.info("[MAIL SERVICE] Starting sending email");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(sender);

        mailSender.send(message);

        log.info("[MAIL SERVICE] Finished sending email");
    }
}