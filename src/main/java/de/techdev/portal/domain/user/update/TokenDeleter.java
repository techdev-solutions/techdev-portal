package de.techdev.portal.domain.user.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
class TokenDeleter {

    @Value("${trackr.oauth.webClientId}")
    private String trackrWebpageClientId = "trackr-page";

    @Autowired
    private TokenStore tokenStore;

    /**
     * Delete all access tokens for a user belonging to the trackr page client.
     */
    void deleteTrackrAngularClientTokens(String username) {
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(trackrWebpageClientId, username);
        for (OAuth2AccessToken token : tokens) {
            tokenStore.removeAccessToken(token);
        }
    }
}
