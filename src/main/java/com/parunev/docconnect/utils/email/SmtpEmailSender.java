package com.parunev.docconnect.utils.email;

import com.parunev.docconnect.security.exceptions.EmailSenderException;
import com.parunev.docconnect.security.payload.EmailError;
import com.parunev.docconnect.utils.DCLogger;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * The {@code SmtpEmailSender} class is responsible for sending emails using the SMTP (Simple Mail Transfer Protocol) server.
 * It implements the {@link EmailSender} interface to provide email sending functionality.
 */
@Service
@ConditionalOnProperty(prefix = "email-sender", name = "client", havingValue = "smtp")
@RequiredArgsConstructor
public class SmtpEmailSender implements EmailSender {

    private final DCLogger dcLogger = new DCLogger(SmtpEmailSender.class);
    private final JavaMailSender mailSender;
    private final HttpServletRequest request;

    /**
     * Sends an email using the SMTP server.
     *
     * @param to      The recipient's email address.
     * @param email   The email content in HTML format.
     * @param subject The email subject.
     * @throws EmailSenderException If an error occurs while sending the email.
     */
    @Override
    @Async
    public void send(String to, String email, String subject) {
        dcLogger.info("Sending email to: " + to);
        try{
            // Create a MimeMessage for the email.
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // Set the email content, recipient, subject, and sender.
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("docconnect.bt@gmail.com");

            // Send the email.
            dcLogger.info("Email sent to: " + to);
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            // Handle email sending failure and log the error.
            dcLogger.error("Failed to send email", e);

            throw new EmailSenderException(EmailError.builder()
                    .path(request.getRequestURI())
                    .error("Failed to send email. %s".formatted(e.getMessage()))
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

}

