package de.techdev.portal.authentication.google;

import org.springframework.security.core.AuthenticationException;

public class GoogleAccessException extends AuthenticationException{
    GoogleAccessException(Exception e) {
        super("Error while accessing the google API", e);
    }
}
