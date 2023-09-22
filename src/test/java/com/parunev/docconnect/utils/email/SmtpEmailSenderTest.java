package com.parunev.docconnect.utils.email;

import com.parunev.docconnect.security.exceptions.EmailSenderException;
import com.parunev.docconnect.security.payload.EmailError;
import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SmtpEmailSenderTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private HttpServletRequest request;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

    @InjectMocks
    private SmtpEmailSender emailSender;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testSend_Success() throws MessagingException, IOException {

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(mimeMessageHelper.getMimeMessage()).thenReturn(mimeMessage);
        when(mimeMessageHelper.getEncoding()).thenReturn("utf-8");
        when(mimeMessageHelper.getMimeMessage().getFrom()).thenReturn(new Address[]{});
        when(mimeMessageHelper.getMimeMessage().getSubject()).thenReturn("Test Subject");
        when(mimeMessageHelper.getMimeMessage().getRecipients(MimeMessage.RecipientType.TO)).thenReturn(new Address[]{});
        when(mimeMessageHelper.getMimeMessage().getContent()).thenReturn("Hello, this is the email.");
        when(mimeMessageHelper.getMimeMessage().getContentType()).thenReturn("text/html");
        when(mimeMessageHelper.getMimeMessage().getHeader("Content-Type")).thenReturn(new String[]{"text/html"});
        when(mimeMessageHelper.getMimeMessage().getHeader("Content-Transfer-Encoding")).thenReturn(new String[]{"quoted-printable"});
        when(mimeMessageHelper.getMimeMessage().getHeader("MIME-Version")).thenReturn(new String[]{"1.0"});
        when(mimeMessageHelper.getMimeMessage().getHeader("From")).thenReturn(new String[]{""});
        when(mimeMessageHelper.getMimeMessage().getHeader("To")).thenReturn(new String[]{""});


        emailSender.send("test@test.com", "Hello, this is the email.", "Test Subject");


        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSend_Failure() {
        when(request.getRequestURI()).thenReturn("/api/v1/auth");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        Mockito.doThrow(new EmailSenderException(EmailError.builder()
                .path(request.getRequestURI())
                .error("Failed to send email.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build()))
                .when(mailSender)
                .send(any(MimeMessage.class));

        assertThrows(EmailSenderException.class, () ->
                emailSender.send("test@test.com", "Hello, this is the email.", "Test Subject")
        );
    }
}
