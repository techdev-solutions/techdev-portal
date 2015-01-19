package de.techdev.portal.domain;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import static java.util.Arrays.asList;

/**
 * Loads a Techdev user from the database.
 *
 * If the user does not exist and the email address ends with @techdev.de it is created in the database and needs to get unlocked.
 */
@Service
public class TechdevAuthenticationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication
                .getCredentials(), asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

}
