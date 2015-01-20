package de.techdev.portal.authentication.google;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import javax.annotation.Resource;

import static java.util.Arrays.asList;

@Configuration
@EnableOAuth2Client
@Profile("google-login")
public class GoogleOauthClientConfiguration {

    @Value("${techdev.portal.google.client-id}")
    private String clientId;

    @Value("${techdev.portal.google.client-secret}")
    private String clientSecret;

    @Value("${techdev.portal.google.redirect-uri}")
    private String redirectUri;

    /**
     * Spring OAuth only allows requests for {@link org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails} when a user is logged in or
     * when it is considered a client (aka our techdev-portal server) request.
     *
     * Google does not allow client_credentials grant requests for a token but needs authorization_code.
     *
     * This is a conflict since we use the request to log the user in. So we just act like this is a clientOnly request for Spring Security.
     */
    static class AuthorizationCodeResourceDetailsWithoutClientOnly extends AuthorizationCodeResourceDetails {
        @Override
        public boolean isClientOnly() {
            return true;
        }
    }

    @Bean
    public OAuth2ProtectedResourceDetails google() {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetailsWithoutClientOnly();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setScope(asList("profile"));
        details.setAccessTokenUri("https://www.googleapis.com/oauth2/v3/token");

        // to add client_id and client_secret to the form when requesting an access token
        details.setClientAuthenticationScheme(AuthenticationScheme.form);

        // The request to obtain an authorization code has to have the same redirect_uri as the request for an access token.
        // This value is used for the access token request.
        details.setPreEstablishedRedirectUri(redirectUri);
        return details;
    }

    @Resource
    @Qualifier("accessTokenRequest")
    private AccessTokenRequest accessTokenRequest;

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2RestTemplate googleRestTemplate() {
        return new OAuth2RestTemplate(google(), new DefaultOAuth2ClientContext(accessTokenRequest));
    }
}
