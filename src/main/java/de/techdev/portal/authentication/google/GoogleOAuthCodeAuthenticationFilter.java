package de.techdev.portal.authentication.google;

import de.techdev.portal.authentication.google.data.PlusEmail;
import de.techdev.portal.authentication.google.data.PlusPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Filter that tries to access Google+ with an authorization code that must be present in the URL
 */
public class GoogleOAuthCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private RestOperations googleRestTemplate;

    public GoogleOAuthCodeAuthenticationFilter(RequestCache requestCache) {
        super("/login");
        // Redirect the user to the original wanted page after a successful login.
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setRequestCache(requestCache);
        this.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (request.getParameter("code") == null) {
            throw new AuthorizationCodeMissingException();
        }

        PlusPerson person = loadPersonFromGoogle();

        Optional<PlusEmail> accountMail = person.getEmails().stream().filter(plusEmail -> plusEmail.getType().equals("account")).findAny();

        if (!accountMail.isPresent()) {
            // signify we don't log in in that case.
            return null;
        }

        Authentication token = new UsernamePasswordAuthenticationToken(accountMail.get().getValue(), "nouseforapassword");
        return this.getAuthenticationManager().authenticate(token);
    }

    private PlusPerson loadPersonFromGoogle() {
        try {
            return googleRestTemplate.getForEntity("https://www.googleapis.com/plus/v1/people/me", PlusPerson.class).getBody();
        } catch (RestClientException e) {
            throw new GoogleAccessException(e);
        }
    }
}
