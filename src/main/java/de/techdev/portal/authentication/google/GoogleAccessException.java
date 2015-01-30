package de.techdev.portal.authentication.google;

import org.springframework.security.core.AuthenticationException;

public class GoogleAccessException extends AuthenticationException{

    GoogleAccessException(String message) {
        super(message);
    }

    public GoogleAccessException(String msg, Throwable t) {
        super(msg, t);
    }
}
