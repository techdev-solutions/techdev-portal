package de.techdev.portal.authentication.google;

import de.techdev.portal.authentication.google.data.PlusEmail;
import de.techdev.portal.authentication.google.data.PlusPerson;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Filter that tries to access Google+ with an access token that must be present in the URL
 */
public class GoogleOAuthCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public GoogleOAuthCodeAuthenticationFilter(RequestCache requestCache) {
        super("/login");
        // Redirect the user to the original wanted page after a successful login.
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setRequestCache(requestCache);
        successHandler.setDefaultTargetUrl("/showUser");
        successHandler.setRedirectStrategy(new GoogleLoginRedirectStrategy());
        this.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (request.getParameter("access-token") == null) {
            throw new AccessTokenMissingException();
        }

        PlusPerson person = loadPersonFromGoogle(request.getParameter("access-token"));

        Optional<PlusEmail> accountMail = person.getEmails().stream().filter(plusEmail -> plusEmail.getType().equals("account")).findAny();

        if (!accountMail.isPresent()) {
            // signify we don't log in in that case.
            return null;
        }

        Authentication token = new UsernamePasswordAuthenticationToken(accountMail.get().getValue(), "nouseforapassword");
        return this.getAuthenticationManager().authenticate(token);
    }

    private PlusPerson loadPersonFromGoogle(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            ResponseEntity<PlusPerson> response = new RestTemplate()
                    .exchange("https://www.googleapis.com/plus/v1/people/me", HttpMethod.GET, new HttpEntity<>(headers), PlusPerson.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new GoogleAccessException("No authorization to access Google with given token.");
            }
            throw new GoogleAccessException("Other error accessing Google.", e);
        }
    }
}
