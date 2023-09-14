package com.parunev.docconnect.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The `DCLogger` class is a utility for logging messages with additional context,
 * such as correlation ID and client IP address. It encapsulates a logger instance
 * and provides methods for logging at different log levels (debug, info, warn, error).
 * <p>
 * Key Features:
 * - Supports standard logging levels: debug, info, warn, error.
 * - Appends correlation ID and client IP address to log messages for added context.
 * - Provides a thread-local mechanism to store and retrieve correlation ID and
 *   HTTP servlet request for each thread.
 * <p>
 * Usage:
 * 1. Create an instance of `DCLogger` for a specific class:
 *    ```java
 *    DCLogger logger = new DCLogger(MyClass.class);
 *    ```
 * 2. Set the logger properties (correlation ID and HTTP servlet request) using
 *    the `setDCLoggerProperties` method before logging messages:
 *    ```java
 *    DCLogger.setDCLoggerProperties(correlationId, request);
 *    ```
 * 3. Use the provided logging methods to log messages with context:
 *    ```java
 *    logger.debug("Debug message");
 *    logger.info("Info message");
 *    logger.warn("Warning message");
 *    logger.error("Error message", exception);
 *    ```
 * <p>
 * Example:
 * ```java
 * // Creating a DCLogger instance and setting logger properties
 * DCLogger logger = new DCLogger(MyClass.class);
 * DCLogger.setDCLoggerProperties(correlationId, request);
 * <p>
 * // Logging messages with context
 * logger.info("User logged in: {}", username);
 * logger.error("An error occurred", exception);
 * ```
 *
 * @see org.slf4j.Logger
 * @see jakarta.servlet.http.HttpServletRequest
 * @see org.slf4j.LoggerFactory
 */
@Data
public class DCLogger {

    /**
     * The logger instance associated with the specified class.
     */
    private final Logger logger;

    /**
     * Thread-local storage for correlation ID.
     */
    private static ThreadLocal<String> correlationIdTL = new ThreadLocal<>();

    /**
     * Thread-local storage for HTTP servlet request.
     */
    private static ThreadLocal<HttpServletRequest> requestTL = new ThreadLocal<>();

    /**
     * Constructs a `DCLogger` instance for the specified class.
     *
     * @param clazz The class for which the logger is created.
     */
    public DCLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    /**
     * Sets the correlation ID and HTTP servlet request properties in the
     * thread-local storage for logging context.
     *
     * @param correlationId The correlation ID associated with the log messages.
     * @param request       The HTTP servlet request associated with the log messages.
     */
    public static void setDCLoggerProperties(String correlationId, HttpServletRequest request){
        correlationIdTL.set(correlationId);
        requestTL.set(request);
    }

    /**
     * Retrieves the current correlation ID from the thread-local storage.
     *
     * @return The current correlation ID or "Unknown" if not set.
     */
    public static String getCorrelationId() {
        return correlationIdTL.get();
    }

    /**
     * Retrieves the current HTTP servlet request from the thread-local storage.
     *
     * @return The current HTTP servlet request or `null` if not set.
     */
    public static HttpServletRequest getRequest() {
        return requestTL.get();
    }

    /**
     * Logs a debug-level message with context.
     *
     * @param message The log message.
     * @param args    The arguments to include in the message.
     */
    public void debug(String message, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(formatMessage(message), args);
        }
    }

    /**
     * Logs an info-level message with context.
     *
     * @param message The log message.
     * @param args    The arguments to include in the message.
     */
    public void info(String message, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(formatMessage(message), args);
        }
    }

    /**
     * Logs a warn-level message with context.
     *
     * @param message The log message.
     * @param args    The arguments to include in the message.
     */
    public void warn(String message, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(formatMessage(message), args);
        }
    }

    /**
     * Logs an error-level message with context.
     *
     * @param message   The log message.
     * @param throwable The exception associated with the error.
     * @param args      The arguments to include in the message.
     */
    public void error(String message, Throwable throwable, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(formatMessage(message), args, throwable);
        }
    }

    /**
     * Formats a log message with context, including the correlation ID and
     * client IP address.
     *
     * @param message The original log message.
     * @return The formatted log message.
     */
    private String formatMessage(String message) {
        return message + " (Correlation ID: " + getCorrelationId() + ", Client IP: " + getClientIp(getRequest()) + ")";
    }

    /**
     * Retrieves the client IP address from the HTTP servlet request.
     *
     * @param request The HTTP servlet request.
     * @return The client IP address or "Unknown" if not available.
     */
    private String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "Unknown";
        }

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }
}
