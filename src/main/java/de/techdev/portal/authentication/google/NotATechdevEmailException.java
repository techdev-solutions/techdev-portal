package de.techdev.portal.authentication.google;

import org.springframework.security.authentication.BadCredentialsException;

public class NotATechdevEmailException extends BadCredentialsException {

    NotATechdevEmailException() {
        super("Not a techdev email address!");
    }
}
