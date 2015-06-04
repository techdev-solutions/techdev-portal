package de.techdev.portal.domain.trackr;

import de.techdev.portal.core.OauthAuthorizationServerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import static java.util.Arrays.asList;

@Configuration
class TrackrConfiguration {

    @Value("${techdev.portal.accessTokenUri}")
    private String accessTokenUri;

    @Value("${techdev.portal.trackr.clientSecret}")
    private String trackrClientSecret;

    @Bean
    public OAuth2RestTemplate trackrTemplate() {
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setId(OauthAuthorizationServerConfiguration.TRACKR_RESOURCE_ID);
        resourceDetails.setClientId(OauthAuthorizationServerConfiguration.TECHDEV_PORTAL_CLIENT);
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientSecret(trackrClientSecret);
        resourceDetails.setScope(asList("read", "write"));
        return new OAuth2RestTemplate(resourceDetails);
    }

}
