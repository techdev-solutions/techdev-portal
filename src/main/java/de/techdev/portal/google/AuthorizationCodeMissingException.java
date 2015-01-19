package de.techdev.portal.google;

import org.springframework.security.core.AuthenticationException;

public class AuthorizationCodeMissingException extends AuthenticationException {

    public AuthorizationCodeMissingException() {
        super("Authorization code is required.");
    }
}
