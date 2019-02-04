package de.techdev.portal.authentication.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * Filter that tries to access Google with an access token that must be present in the URL
 */
public class GoogleOAuthCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public GoogleOAuthCodeAuthenticationFilter(RequestCache requestCache, HttpTransport httpTransport, String googleClientId) {
        super("/login");
        // Redirect the user to the original wanted page after a successful login.
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setRequestCache(requestCache);
        successHandler.setDefaultTargetUrl("/landing");
        successHandler.setRedirectStrategy(new GoogleLoginRedirectStrategy());
        this.setAuthenticationSuccessHandler(successHandler);
        googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(httpTransport, new JacksonFactory())
            .setAudience(Collections.singleton(googleClientId))
            .build();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (request.getParameter("access-token") == null) {
            throw new AccessTokenMissingException();
        }

        GoogleIdToken idToken;
        try {
            idToken = googleIdTokenVerifier.verify(request.getParameter("access-token"));
        } catch (GeneralSecurityException e) {
            throw new GoogleAccessException("Error verifying token", e);
        }
        if (idToken == null) {
            // Google library returns null if token couldn't be verified - translate to an authentication error.
            throw new BadCredentialsException("Token verification failed");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String accountMail = payload.getEmail();

        Authentication token = new UsernamePasswordAuthenticationToken(accountMail, "nouseforapassword");
        return this.getAuthenticationManager().authenticate(token);
    }
}
