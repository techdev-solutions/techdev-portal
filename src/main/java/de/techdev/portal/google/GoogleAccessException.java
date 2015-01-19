package de.techdev.portal.google;

import org.springframework.security.core.AuthenticationException;

public class GoogleAccessException extends AuthenticationException{
    public GoogleAccessException(Exception e) {
        super("Error while accessing the google API", e);
    }
}
