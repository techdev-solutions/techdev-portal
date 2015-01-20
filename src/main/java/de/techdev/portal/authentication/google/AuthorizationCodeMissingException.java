package de.techdev.portal.authentication.google;

import org.springframework.security.core.AuthenticationException;

public class AuthorizationCodeMissingException extends AuthenticationException {

    AuthorizationCodeMissingException() {
        super("Authorization code is required.");
    }
}
