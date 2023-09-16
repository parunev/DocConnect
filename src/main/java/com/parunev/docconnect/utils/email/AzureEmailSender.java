package com.parunev.docconnect.utils.email;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.models.EmailMessage;
import com.azure.communication.email.models.EmailSendResult;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;
import com.parunev.docconnect.security.exceptions.EmailSenderException;
import com.parunev.docconnect.security.payload.EmailError;
import com.parunev.docconnect.utils.DCLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * The {@code AzureEmailSender} class is responsible for sending emails using the Azure Communication Service.
 * It implements the {@link EmailSender} interface to provide email sending functionality.
 */
@Service
@ConditionalOnProperty(prefix = "email-sender", name = "client", havingValue = "azure")
@RequiredArgsConstructor
public class AzureEmailSender implements EmailSender{

    private final DCLogger dcLogger = new DCLogger(AzureEmailSender.class);
    private final EmailClient emailClient;
    private final HttpServletRequest request;

    @Value("${azure.communication.from}")
    private String senderAddress;

    /**
     * Sends an email using the Azure Communication Service.
     *
     * @param to      The recipient's email address.
     * @param email   The email content in HTML format.
     * @param subject The email subject.
     * @throws EmailSenderException If an error occurs while sending the email.
     */
    @Override
    public void send(String to, String email, String subject) {
        try {
            // Create an email message with sender, recipient, subject, and email body.
            EmailMessage emailMessage = new EmailMessage()
                    .setSenderAddress(senderAddress)
                    .setToRecipients(to)
                    .setSubject(subject)
                    .setBodyHtml(email);

            // Initiate the email sending process and wait for completion.
            SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(emailMessage);
            PollResponse<EmailSendResult> response = poller.waitForCompletion();

            if (response.getStatus().isComplete()) {
                dcLogger.info("Email sent to: " + to);
            }

        } catch (Exception e) {
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

