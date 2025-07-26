package com.seemee.userservice.service.impl;

import com.seemee.userservice.model.User;
import com.seemee.userservice.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void sendWelcomeEmail(User user) {
        try {
            // Validate email configuration before sending
            if (fromEmail == null || fromEmail.contains("your-email")) {
                logger.warn("Email configuration not properly set. Skipping email for: {}", user.getEmail());
                return;
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Welcome to SeeMe - Account Created Successfully!");

            String emailBody = buildWelcomeEmailBody(user);
            message.setText(emailBody);

            mailSender.send(message);
            logger.info("Welcome email sent successfully to: {}", user.getEmail());

        } catch (Exception e) {
            logger.error("Failed to send welcome email to: {}. Error: {}", user.getEmail(), e.getMessage());
            logger.error("Email configuration check: fromEmail={}, frontendUrl={}", fromEmail, frontendUrl);
            // Don't throw exception to avoid breaking user creation process
        }
    }

    private String buildWelcomeEmailBody(User user) {
        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(user.getFirstName() != null ? user.getFirstName() : "User").append(",\n\n");
        body.append("Welcome to SeeMe! Your account has been created successfully.\n\n");
        body.append("You can now login to your account using the following link:\n");
        body.append(frontendUrl).append("\n\n");
        body.append("Login using your registered email: ").append(user.getEmail()).append("\n\n");
        body.append("Thank you for joining SeeMe!\n\n");
        body.append("Best regards,\n");
        body.append("The SeeMe Team");

        return body.toString();
    }
}
