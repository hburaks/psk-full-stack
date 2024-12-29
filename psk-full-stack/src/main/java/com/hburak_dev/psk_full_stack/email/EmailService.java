package com.hburak_dev.psk_full_stack.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject,
            String sessionDateStr
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        if (sessionDateStr != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");
                LocalDateTime sessionDate = LocalDateTime.parse(sessionDateStr, formatter);
                properties.put("sessionDate", sessionDate);
            } catch (Exception e) {
                log.error("Error parsing date: {}", sessionDateStr, e);
            }
        }

        Context context = new Context();
        context.setVariables(properties);

        String htmlContent = templateEngine.process(emailTemplate.getName(), context);
        helper.setText(htmlContent, true);

        helper.setFrom(senderEmail);
        helper.setTo(to);
        helper.setSubject(subject);

        mailSender.send(mimeMessage);
        log.info("Email sent to: {}", to);
    }
}
