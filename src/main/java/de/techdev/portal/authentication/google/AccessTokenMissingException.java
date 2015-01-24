package de.techdev.portal.authentication.google;

import org.springframework.security.core.AuthenticationException;

public class AccessTokenMissingException extends AuthenticationException {

    AccessTokenMissingException() {
        super("The access token is required.");
    }
}
