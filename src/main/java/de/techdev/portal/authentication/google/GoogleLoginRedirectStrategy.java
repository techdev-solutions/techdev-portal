package de.techdev.portal.authentication.google;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.util.UrlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Redirect strategy that sends the redirect URL in the body instead of as a HTTP redirect - useful if a Javascript redirect is necessary.
 */
public class GoogleLoginRedirectStrategy extends DefaultRedirectStrategy {
    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        response.getWriter().append(calculateRedirectUrl(request.getContextPath(), url));
        response.flushBuffer();
    }

    private String calculateRedirectUrl(String contextPath, String url) {
        if (!UrlUtils.isAbsoluteUrl(url)) {
            return contextPath + url;
        }
        return url;
    }
}
