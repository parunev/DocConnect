package com.parunev.docconnect.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.io.*;
import java.util.Base64;
import java.util.Optional;

/**
 * Utility class for working with HTTP cookies, serialization, and deserialization.
 * This class provides methods to handle cookies, including retrieving, adding, and deleting them from
 * HTTP requests and responses. It also offers functionality to serialize and deserialize objects
 * to and from Base64-encoded strings.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtils {

    /**
     * Retrieve a cookie by name from an HTTP request.
     *
     * @param request The HTTP request from which to retrieve the cookie.
     * @param name    The name of the cookie to retrieve.
     * @return An Optional containing the requested cookie, or an empty Optional if not found.
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Add a cookie to an HTTP response.
     *
     * @param response The HTTP response to which to add the cookie.
     * @param name     The name of the cookie.
     * @param value    The value of the cookie.
     * @param maxAge   The maximum age (in seconds) for the cookie.
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * Delete a cookie from an HTTP request and response.
     *
     * @param request  The HTTP request from which to delete the cookie.
     * @param response The HTTP response to which to add the deletion request.
     * @param name     The name of the cookie to delete.
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * Serialize an object to a Base64-encoded string.
     *
     * @param object The object to serialize.
     * @return A Base64-encoded string representing the serialized object.
     * @throws IOException If an I/O error occurs during serialization.
     */
    public static String serialize(OAuth2AuthorizationRequest object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return Base64.getUrlEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    /**
     * Deserialize an object from a Base64-encoded cookie.
     *
     * @param cookie The cookie containing the Base64-encoded object.
     * @param cls    The class type to which the object should be deserialized.
     * @param <T>    The type of the deserialized object.
     * @return The deserialized object.
     * @throws IOException            If an I/O error occurs during deserialization.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    public static <T> T deserialize(Cookie cookie, Class<T> cls) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getUrlDecoder().decode(cookie.getValue());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object deserializedObject = objectInputStream.readObject();
        objectInputStream.close();
        return cls.cast(deserializedObject);
    }

}
