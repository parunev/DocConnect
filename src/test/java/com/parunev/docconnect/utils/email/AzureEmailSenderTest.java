package com.parunev.docconnect.utils.email;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.models.EmailSendResult;
import com.azure.core.util.polling.LongRunningOperationStatus;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;
import com.parunev.docconnect.security.exceptions.EmailSenderException;
import com.parunev.docconnect.security.payload.EmailError;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AzureEmailSenderTest {

    @Mock
    private EmailClient emailClient;

    @Mock
    private SyncPoller<EmailSendResult, EmailSendResult> poller;

    @Mock
    private HttpServletRequest request;

    @Mock
    private PollResponse<EmailSendResult> pollResponse;

    @InjectMocks
    private AzureEmailSender emailSender;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSend_Success() {
        when(emailClient.beginSend(any())).thenReturn(poller);
        when(poller.waitForCompletion()).thenReturn(pollResponse);
        when(pollResponse.getStatus()).thenReturn(LongRunningOperationStatus.SUCCESSFULLY_COMPLETED);

        assertEquals(LongRunningOperationStatus.SUCCESSFULLY_COMPLETED, pollResponse.getStatus());

        emailSender.send("test@test.com", "Hello, this is the email.", "Test Subject");
    }

    @Test
    void testSend_Failure() {
        when(request.getRequestURI()).thenReturn("/api/v1/auth");
        when(emailClient.beginSend(any())).thenReturn(poller);
        when(poller.waitForCompletion()).thenReturn(pollResponse);

        doThrow(new EmailSenderException(EmailError.builder()
                .path(request.getRequestURI())
                .error("Failed to send email.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build()));

        assertThrows(EmailSenderException.class, () -> emailSender.send("test@test.com", "Hello, this is the email.", "Test Subject"));

    }
}
