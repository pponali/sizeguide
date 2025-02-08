package com.sizeguide.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j

@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${notification.email}")
    private String notificationEmail;

    public void sendValidationResultEmail(String sizeGuideId, Object validationResult) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(notificationEmail);
            message.setSubject("Size Guide Validation Result: " + sizeGuideId);
            message.setText("Validation results for size guide " + sizeGuideId + ":\n\n" + validationResult.toString());
            
            mailSender.send(message);
            log.info("Validation result email sent successfully for size guide: {}", sizeGuideId);
        } catch (Exception e) {
            log.error("Failed to send validation result email for size guide: {}", sizeGuideId, e);
        }
    }
}
