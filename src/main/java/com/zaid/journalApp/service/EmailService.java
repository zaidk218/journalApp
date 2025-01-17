package com.zaid.journalApp.service;

import com.zaid.journalApp.dto.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }
    @Async
    public void sendEmail(EmailDto emailDto) {
        try {
            log.debug("Starting to send email to: {}", emailDto.getTo());

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(emailDto.getTo());
            helper.setSubject(emailDto.getSubject());
            helper.setText(emailDto.getBody(), emailDto.isHtml());

            emailSender.send(message);

            log.info("Email sent successfully to: {}", emailDto.getTo());
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", emailDto.getTo(), e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendWelcomeEmail(String to, String username) {
        EmailDto emailDto = EmailDto.builder()
                .to(to)
                .subject("Welcome to Journal App!")
                .body(buildWelcomeEmailTemplate(username))
                .isHtml(true)
                .build();

        sendEmail(emailDto);
    }

    public void sendPasswordResetEmail(String to, String resetToken) {
        EmailDto emailDto = EmailDto.builder()
                .to(to)
                .subject("Password Reset Request")
                .body(buildPasswordResetTemplate(resetToken))
                .isHtml(true)
                .build();

        sendEmail(emailDto);
    }

    private String buildWelcomeEmailTemplate(String username) {
        return """
            <div style="font-family: Arial, sans-serif; padding: 20px;">
                <h2>Welcome to Journal App, %s!</h2>
                <p>Thank you for joining our community. We're excited to have you on board.</p>
                <p>With Journal App, you can:</p>
                <ul>
                    <li>Create personal journal entries</li>
                    <li>Track your mood over time</li>
                    <li>Get sentiment analysis insights</li>
                </ul>
                <p>If you have any questions, feel free to reply to this email.</p>
                <p>Best regards,<br>The Journal App Team</p>
            </div>
            """.formatted(username);
    }

    private String buildPasswordResetTemplate(String resetToken) {
        return """
            <div style="font-family: Arial, sans-serif; padding: 20px;">
                <h2>Password Reset Request</h2>
                <p>You've requested to reset your password. Click the link below to proceed:</p>
                <p><a href="http://your-app.com/reset-password?token=%s">Reset Password</a></p>
                <p>If you didn't request this, please ignore this email.</p>
                <p>Best regards,<br>The Journal App Team</p>
            </div>
            """.formatted(resetToken);
    }
}
