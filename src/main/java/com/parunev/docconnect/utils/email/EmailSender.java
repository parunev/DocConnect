package com.parunev.docconnect.utils.email;

/**
 * An interface for sending emails.
 */
public interface EmailSender {

    /**
     * Send an email to the specified address.
     * @param to The address to send the email to.
     * @param email The email to send.
     * @param subject The subject of the email.
     */

    void send(String to, String email, String subject);
}

